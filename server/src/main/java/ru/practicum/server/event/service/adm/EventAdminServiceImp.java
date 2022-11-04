package ru.practicum.server.event.service.adm;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.AdminUpdateEventRequestDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestError;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.event.EventMapper.toEventFullDto;

/**
 * реализация интерфейса администратора по работе с данными событий
 * @see EventAdminService
 * */
@Service
@Slf4j
public class EventAdminServiceImp implements EventAdminService {

    /**
     * репозиторий событий
     * @see EventRepository
     * */
    private final EventRepository eventRepository;

    /**
     * репозиторий пользователей
     * @see UserRepository
     * */
    private final UserRepository userRepository;

    /**
     * репозиторий категорий
     * @see CategoryRepository
     * */
    private final CategoryRepository categoryRepository;

    /**
     * репозиторий запросов на участие в событиях
     * @see RequestRepository
     * */
    private final RequestRepository requestRepository;

    /**
     * клиент для взаимодействия с сервисом статистики
     * @see EventClient
     * */
    private final EventClient eventClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public EventAdminServiceImp(EventRepository eventRepository, UserRepository userRepository,
                                CategoryRepository categoryRepository, RequestRepository requestRepository,
                                EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @see EventAdminService#getAllEvents(List, List, List, LocalDateTime, LocalDateTime, PageRequest)
     * @param states - не должен быть null
     * @param categories - не должен быть null
     * @param users - не должен быть null
     * */
    @Override
    public List<EventFullDto> getAllEvents(@NonNull List<String> states, @NonNull List<Integer> categories, @NonNull List<Integer> users, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, PageRequest pageRequest) {
        List<Event> events = new ArrayList<>();
        for (String state : states) {
            for (Integer categoryId : categories) {
                for (Integer userId : users) {
                    events.addAll(eventRepository.getEventsForAdmin(state, categoryId.longValue(), userId.longValue(), rangeStart,
                            rangeEnd, pageRequest).toList());
                }
            }
        }
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : events) {
            User user = userRepository.findById(event.getInitiatorId()).get();
            Category category = categoryRepository.findById(event.getCategoryId()).get();
            eventFullDtos.add(toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(event.getId())));
        }
        log.debug("Предоставлены данные по событиям");
        return eventFullDtos;
    }

    /**
     * @see EventAdminService#updateEvent(long, AdminUpdateEventRequestDto)
     * @param adminUpdateEventRequestDto - не должен быть null
     * */
    @Override
    public EventFullDto updateEvent(long eventId, @NonNull AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (adminUpdateEventRequestDto.getEventDate() != null) {
            checkCreationTime(adminUpdateEventRequestDto.getEventDate());
            event.setEventDate(LocalDateTime.parse(adminUpdateEventRequestDto.getEventDate(), formatter));
        }
        if (adminUpdateEventRequestDto.getTitle() != null) {
            event.setTitle(adminUpdateEventRequestDto.getTitle());
        }
        if (adminUpdateEventRequestDto.getDescription() != null) {
            event.setDescription(adminUpdateEventRequestDto.getDescription());
        }
        if (adminUpdateEventRequestDto.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequestDto.getAnnotation());
        }
        if (adminUpdateEventRequestDto.getCategory() > 0) {
            event.setCategoryId(adminUpdateEventRequestDto.getCategory());
        }
        if (adminUpdateEventRequestDto.getParticipantLimit() > 0) {
            if (requestRepository.getEventParticipantLimit(eventId)
                    > adminUpdateEventRequestDto.getParticipantLimit()) {
                log.warn("Обновление лимита участников события недоступно, так как подтвержденных запросов больше " +
                        "предлагаемого лимита. Отмените ряд запросов");
            } else {
                event.setParticipantLimit(adminUpdateEventRequestDto.getParticipantLimit());
                event.setLimit(false);
            }
        }
        event.setPaid(adminUpdateEventRequestDto.isPaid());
        log.debug("Данные события обновлены: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(eventId));
    }

    /**
     * @see EventAdminService#publishEvent(long)
     * */
    @Override
    public EventFullDto publishEvent(long eventId) {
        checkEvent(eventId);
        checkEventParam(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(State.PUBLISHED);
        log.debug("Опубликовано событие: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(eventRepository.save(event), user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId()));
    }

    /**
     * @see EventAdminService#rejectEvent(long)
     * */
    @Override
    public EventFullDto rejectEvent(long eventId) {
        checkEvent(eventId);
        checkEventParam(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(State.CANCELED);
        eventRepository.save(event);
        log.debug("Отклонено событие: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId()));
    }

    /**
     * метод проверки наличия события по ID в репозитории
     * @throws NotFoundError - при отсутствии события по ID
     * */
    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new NotFoundError("Событие ID: " + eventId + ", не найдено!");
        }
    }

    /**
     * метод проверки параметров публикации события
     * @throws BadRequestError - при нарушении даты назначения события и нарушения условия статуса события
     * */
    public void checkEventParam(long eventId) {
        if (eventRepository.findById(eventId).get().getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestError("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
        if (eventRepository.findById(eventId).get().getState() != State.PENDING) {
            log.warn("Событие должно быть в состоянии ожидании публикации");
            throw new BadRequestError("Событие должно быть в состоянии ожидании публикации");
        }
    }

    /**
     * метод проверки редактирования даты начала события
     * @throws BadRequestError - при нарушении даты назначения события
     * */
    public void checkCreationTime(String time) {
        if (LocalDateTime.parse(time, formatter).isBefore(LocalDateTime.now().plusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestError("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
    }
}
