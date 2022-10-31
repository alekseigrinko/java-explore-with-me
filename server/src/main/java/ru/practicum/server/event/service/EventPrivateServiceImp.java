package ru.practicum.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.LocationRepository;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.UpdateEventRequest;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.Location;
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
import java.util.stream.Collectors;

import static ru.practicum.server.event.EventMapper.*;

@Service
@Slf4j
public class EventPrivateServiceImp implements EventPrivateService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
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

    @Override
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        checkCreationTime(newEventDto.getEventDate());
        checkUser(userId);
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(newEventDto.getCategory()).get();
        log.debug("Добавлено событие: " + newEventDto.getTitle());
        return toEventFullDto(eventRepository.save(toEvent(newEventDto, userId)), user, category,
                0, 0);
    }

    @Override
    public EventFullDto updateEvent(long userId, UpdateEventRequest updateEventRequest) {
        checkUser(userId);
        checkEvent(updateEventRequest.getEventId());
        Event event = eventRepository.findById(updateEventRequest.getEventId()).get();
        if (updateEventRequest.getEventDate() != null) {
            checkCreationTime(updateEventRequest.getEventDate());
            event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), formatter));
        }
        if (userId != event.getInitiatorId()) {
            log.warn("Пользователь не имеет прав для редактирования событий");
            throw new BadRequestException("Пользователь не имеет прав для редактирования событий");
        }
        if (eventRepository.findById(updateEventRequest.getEventId()).get().isRequestModeration()) {
            log.warn("Событие уже подтверждено и не подлежит изменению");
            throw new BadRequestException("Событие уже подтверждено и не подлежит изменению");
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() > 0) {
            event.setCategoryId(updateEventRequest.getCategory());
        }
        if (updateEventRequest.getParticipantLimit() > 0) {
            if (requestRepository.getEventParticipantLimit(updateEventRequest.getEventId())
                    > updateEventRequest.getParticipantLimit()) {
                log.warn("Обновление лимита участников события недоступно, так как подтвержденных запросов больше " +
                        "предлагаемого лимита. Отмените ряд запросов");
            } else {
                event.setParticipantLimit(updateEventRequest.getParticipantLimit());
                event.setLimit(false);
            }
        }
        event.setPaid(updateEventRequest.isPaid());
        log.debug("Данные события обновлены: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(updateEventRequest.getEventId()));
    }

    @Override
    public EventFullDto cancelEvent(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (!event.isRequestModeration()) {
            log.warn("Событие событие должно быть в состоянии модерации");
            throw new BadRequestException("Событие событие должно быть в состоянии модерации");
        }
        event.setState(State.CANCELED);
        log.debug("Данные события обновлены: " + event);
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventFullDto(eventRepository.save(event), user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(eventId));
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

    public void checkCreationTime(String time) {
        if (LocalDateTime.parse(time, formatter).isBefore(LocalDateTime.now().minusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestException("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
    }
}
