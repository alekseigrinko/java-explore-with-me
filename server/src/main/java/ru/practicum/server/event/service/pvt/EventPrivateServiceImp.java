package ru.practicum.server.event.service.pvt;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.UpdateEventRequestDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestError;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.event.EventMapper.*;

/**
 * реализация публичного интерфейса по работе с данными событий
 * @see EventPrivateService
 * */
@Service
@Slf4j
public class EventPrivateServiceImp implements EventPrivateService {

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

    public EventPrivateServiceImp(EventRepository eventRepository, UserRepository userRepository,
                                  CategoryRepository categoryRepository, RequestRepository requestRepository,
                                  EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @see EventPrivateService#getAllUserEvents(long, PageRequest)
     * */
    @Override
    public List<EventFullDto> getAllUserEvents(long userId, PageRequest pageRequest) {
        checkUser(userId);
        List<Event> events = eventRepository.getAllUserEvents(userId, pageRequest).stream()
                .collect(Collectors.toList());
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : events) {
            User user = userRepository.findById(userId).get();
            Category category = categoryRepository.findById(event.getCategoryId()).get();
            eventFullDtos.add(toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(event.getId())));
        }
        log.debug("Данные предоставлены по событиям пользователя ID: " + userId);
        return eventFullDtos;
    }

    /**
     * @see EventPrivateService#getUserEventById(long, long)
     * */
    @Override
    public EventFullDto getUserEventById(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        log.debug("Данные предоставлены по событию ID: " + eventId);
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(eventId));
    }

    /**
     * @see EventPrivateService#addEvent(long, NewEventDto)
     * @param newEventDto - не должно быть равно null
     * */
    @Override
    public EventFullDto addEvent(long userId, @NonNull NewEventDto newEventDto) {
        checkCreationTime(newEventDto.getEventDate());
        checkUser(userId);
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(newEventDto.getCategory()).get();
        log.debug("Добавлено событие: " + newEventDto.getTitle());
        return toEventFullDto(eventRepository.save(toEvent(newEventDto, userId)), user, category,
                0, 0);
    }

    /**
     * @see EventPrivateService#updateEvent(long, UpdateEventRequestDto)
     * @param updateEventRequestDto - не должно быть равно null
     * */
    @Override
    public EventFullDto updateEvent(long userId, @NonNull UpdateEventRequestDto updateEventRequestDto) {
        checkUser(userId);
        checkEvent(updateEventRequestDto.getEventId());
        Event event = eventRepository.findById(updateEventRequestDto.getEventId()).get();
        if (updateEventRequestDto.getEventDate() != null) {
            checkCreationTime(updateEventRequestDto.getEventDate());
            event.setEventDate(LocalDateTime.parse(updateEventRequestDto.getEventDate(), formatter));
        }
        if (userId != event.getInitiatorId()) {
            log.warn("Пользователь не имеет прав для редактирования событий");
            throw new BadRequestError("Пользователь не имеет прав для редактирования событий");
        }
        if (updateEventRequestDto.getTitle() != null) {
            event.setTitle(updateEventRequestDto.getTitle());
        }
        if (updateEventRequestDto.getDescription() != null) {
            event.setDescription(updateEventRequestDto.getDescription());
        }
        if (updateEventRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventRequestDto.getAnnotation());
        }
        if (updateEventRequestDto.getCategory() > 0) {
            event.setCategoryId(updateEventRequestDto.getCategory());
        }
        if (updateEventRequestDto.getParticipantLimit() > 0) {
            if (requestRepository.getEventParticipantLimit(updateEventRequestDto.getEventId())
                    > updateEventRequestDto.getParticipantLimit()) {
                log.warn("Обновление лимита участников события недоступно, так как подтвержденных запросов больше " +
                        "предлагаемого лимита. Отмените ряд запросов");
            } else {
                event.setParticipantLimit(updateEventRequestDto.getParticipantLimit());
                event.setLimit(false);
            }
        }
        event.setPaid(updateEventRequestDto.isPaid());
        log.debug("Данные события обновлены: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(updateEventRequestDto.getEventId()));
    }

    /**
     * @see EventPrivateService#cancelEvent(long, long)
     * */
    @Override
    public EventFullDto cancelEvent(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(State.CANCELED);
        log.debug("Данные события обновлены: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(eventRepository.save(event), user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(eventId));
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
     * метод проверки наличия пользователя по ID в репозитории
     * @throws NotFoundError - при отсутствии пользователя по ID
     * */
    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new NotFoundError("Пользователь ID: " + userId + ", не найден!");
        }
    }

    /**
     * метод проверки даты начала события при редактировании события
     * @throws BadRequestError - при нарушении условия изменения даты начала события
     * */
    public void checkCreationTime(@NotBlank String time) {
        if (LocalDateTime.parse(time, formatter).isBefore(LocalDateTime.now().plusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestError("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
    }
}
