package ru.practicum.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.AdminUpdateEventRequest;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.event.EventMapper.toEventFullDto;

@Service
@Slf4j
public class EventAdminServiceImp implements EventAdminService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
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

    @Override
    public List<EventFullDto> getAllEvents(List<String> states, List<Integer> categories, List<Integer> users, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, PageRequest pageRequest) {
        List<Event> events = new ArrayList<>();
        for (String state : states) {
            for (Integer categoryId : categories) {
                for (Integer userId : users) {
                    events.addAll(eventRepository.getEventsForAdmin(state, categoryId, userId, rangeStart,
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

    @Override
    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (adminUpdateEventRequest.getEventDate() != null) {
            checkCreationTime(adminUpdateEventRequest.getEventDate());
            event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));
        }
        if (eventRepository.findById(eventId).get().isRequestModeration()) {
            log.warn("Событие уже подтверждено и не подлежит изменению");
            throw new BadRequestException("Событие уже подтверждено и не подлежит изменению");
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() > 0) {
            event.setCategoryId(adminUpdateEventRequest.getCategory());
        }
        if (adminUpdateEventRequest.getParticipantLimit() > 0) {
            if (requestRepository.getEventParticipantLimit(eventId)
                    > adminUpdateEventRequest.getParticipantLimit()) {
                log.warn("Обновление лимита участников события недоступно, так как подтвержденных запросов больше " +
                        "предлагаемого лимита. Отмените ряд запросов");
            } else {
                event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
                event.setLimit(false);
            }
        }
        event.setPaid(adminUpdateEventRequest.isPaid());
        log.debug("Данные события обновлены: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(eventId));
    }

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

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new ObjectNotFoundException("Событие ID: " + eventId + ", не найдено!");
        }
    }

    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }

    public void checkEventParam(long eventId) {
        if (eventRepository.findById(eventId).get().getEventDate().isAfter(LocalDateTime.now().minusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestException("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
        if (eventRepository.findById(eventId).get().getState() != State.PENDING) {
            log.warn("Событие должно быть в состоянии ожидании публикации");
            throw new BadRequestException("Событие должно быть в состоянии ожидании публикации");
        }
    }

    public void checkCreationTime(String time) {
        if (LocalDateTime.parse(time, formatter).isBefore(LocalDateTime.now().minusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestException("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
    }
}
