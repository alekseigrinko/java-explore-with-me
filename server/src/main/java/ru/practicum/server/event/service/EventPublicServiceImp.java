package ru.practicum.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.LocationRepository;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.Location;
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
public class EventPublicServiceImp implements EventPublicService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public EventPublicServiceImp(EventRepository eventRepository, LocationRepository locationRepository,
                                 CategoryRepository categoryRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EventResponseDto> getAllEvents(String text,List<Integer> categories, boolean paid, LocalDateTime rangeStart,
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
    public EventResponseDto getEvent(long eventId) {
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        Location location = locationRepository.findById(event.getLocationId()).get();
        log.debug("Предоставлены данные по событию ID: " + eventId);
        return toEventResponseDto(event, user, category, location);
    }

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new ObjectNotFoundException("Событие ID: " + eventId + ", не найдено!");
        }
    }
}
