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

import static ru.practicum.server.event.EventMapper.toEventResponseDto;

@Service
@Slf4j
public class EventAdminServiceImp implements EventAdminService {

    public final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public EventAdminServiceImp(EventRepository eventRepository, LocationRepository locationRepository,
                                UserRepository userRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<EventResponseDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, boolean onlyAvailable, PageRequest pageRequest) {
        List<Event> events = eventRepository.getPublicAllEvents(text, LocalDateTime.now(), pageRequest).stream()
                .collect(Collectors.toList());
        List<EventResponseDto> eventResponseDtos = new ArrayList<>();
        for (Event event : events) {
            User user = userRepository.findById(event.getInitiatorId()).get();
            Category category = categoryRepository.findById(event.getCategoryId()).get();
            Location location = locationRepository.findById(event.getLocationId()).get();
            eventResponseDtos.add(toEventResponseDto(event, user, category, location));
        }
        log.debug("Предоставлены данные по событиям");
        return eventResponseDtos;
    }

    @Override
    public EventResponseDto updateEvent(long eventId, EventDto eventDto) {
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
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
        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isAfter(LocalDateTime.now().minusHours(2))) {
                log.warn("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
                throw new BadRequestException("Событие не может быть отредактировано меньше чем за 2 часа до его начала");
            }
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
        if (eventDto.getCategoryId() > 0) {
            event.setCategoryId(eventDto.getCategoryId());
        }
        if (eventDto.getParticipantLimit() > 0) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        event.setPaid(eventDto.isPaid());
        event.setRequestModeration(eventDto.isRequestModeration());
        log.debug("Данные события обновлены: " + event);
        Location location = locationRepository.findById(event.getLocationId()).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventResponseDto(event, user, category, location);
    }

    @Override
    public EventResponseDto publishEvent(long eventId) {
        checkEvent(eventId);
        checkEventParam(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(State.PUBLISHER);
        log.debug("Опубликовано событие: " + event);
        Location location = locationRepository.findById(event.getLocationId()).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventResponseDto(eventRepository.save(event), user, category, location);
    }

    @Override
    public EventResponseDto rejectEvent(long eventId) {
        checkEvent(eventId);
        checkEventParam(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(State.CANCELED);
        eventRepository.save(event);
        log.debug("Отклонено событие: " + event);
        Location location = locationRepository.findById(event.getLocationId()).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventResponseDto(event, user, category, location);
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
}
