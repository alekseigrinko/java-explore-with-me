package ru.practicum.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.LocationRepository;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.Location;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.event.EventMapper.*;

@Service
@Slf4j
public class EventPrivateServiceImp implements EventPrivateService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public EventPrivateServiceImp(EventRepository eventRepository, LocationRepository locationRepository,
                                  UserRepository userRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<EventResponseDto> getAllUserEvents(long userId, PageRequest pageRequest) {
        checkUser(userId);
        List<Event> events = eventRepository.getAllUserEvents(userId, pageRequest).stream()
                .collect(Collectors.toList());
        List<EventResponseDto> eventResponseDtos = new ArrayList<>();
        for (Event event : events) {
            User user = userRepository.findById(userId).get();
            Category category = categoryRepository.findById(event.getCategoryId()).get();
            Location location = locationRepository.findById(event.getLocationId()).get();
            eventResponseDtos.add(toEventResponseDto(event, user, category, location));
        }
        log.debug("Данные предоставлены по событиям пользователя ID: " + userId);
        return eventResponseDtos;
    }

    @Override
    public EventResponseDto getUserEventById(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        Location location = locationRepository.findById(event.getLocationId()).get();
        log.debug("Данные предоставлены по событию ID: " + eventId);
        return toEventResponseDto(event, user, category, location);
    }

    @Override
    public EventResponseDto addEvent(long userId, EventDto eventDto) {
        checkCreationTime(eventDto);
        Location location = new Location(
                null,
                eventDto.getLocation().getLat(),
                eventDto.getLocation().getLon()
        );
        User user = userRepository.findById(userId).get();
        Category category = categoryRepository.findById(eventDto.getCategoryId()).get();
        location = locationRepository.save(location);
        log.debug("Добавлено событие: " + eventDto.getTitle());
        return toEventResponseDto(eventRepository.save(toEvent(eventDto, location.getId())), user, category, location);
    }

    @Override
    public EventResponseDto updateEvent(long userId, long eventId, EventDto eventDto) {
        checkUser(userId);
        checkEvent(eventId);
        checkCreationTime(eventDto);
        Event event = eventRepository.findById(eventId).get();
        if (userId != event.getInitiatorId()) {
            log.warn("Пользователь не имеет прав для редактирования событий");
            throw new BadRequestException("Пользователь не имеет прав для редактирования событий");
        }
        if (eventDto.isRequestModeration()) {
            log.warn("Событие уже подтверждено и не подлежит изменению");
            throw new BadRequestException("Событие уже подтверждено и не подлежит изменению");
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getState() != null) {
            event.setState(eventDto.getState());
        }
        if (eventDto.getCategoryId() > 0) {
            event.setCategoryId(eventDto.getCategoryId());
        }
        event.setRequestModeration(eventDto.isRequestModeration());
        if (eventDto.getCreatedOn() != null) {
            event.setCreatedOn(eventDto.getCreatedOn());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            Location location = locationRepository.save(new Location(
                    null,
                    eventDto.getLocation().getLat(),
                    eventDto.getLocation().getLon()
            ));
            locationRepository.deleteById(event.getLocationId());
            event.setLocationId(location.getId());
        }
        log.debug("Данные события обновлены: " + event);
        Location location = locationRepository.findById(event.getLocationId()).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventResponseDto(event, user, category, location);
    }

    @Override
    public EventResponseDto cancelEvent(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (!event.isRequestModeration()) {
            log.warn("Событие событие должно быть в состоянии модерации");
            throw new BadRequestException("Событие событие должно быть в состоянии модерации");
        }
        event.setState(State.CANCELED);
        log.debug("Данные события обновлены: " + event);
        Location location = locationRepository.findById(event.getLocationId()).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventResponseDto(eventRepository.save(event), user, category, location);
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

    public void checkCreationTime(EventDto eventDto) {
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            throw new BadRequestException("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
        }
    }
}
