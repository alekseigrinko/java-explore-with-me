package ru.practicum.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.LocationRepository;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.Location;
import ru.practicum.server.event.model.SortEvent;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final RequestRepository requestRepository;
    private final EventClient eventClient;

    public EventPublicServiceImp(EventRepository eventRepository, LocationRepository locationRepository,
                                 CategoryRepository categoryRepository, UserRepository userRepository, RequestRepository requestRepository, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    @Override
    public List<EventResponseDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort, PageRequest pageRequest) {
        List<Event> events = new ArrayList<>();
        for (Integer categoryId : categories) {
            events.addAll(eventRepository.getPublicAllEvents(text, categoryId.longValue(), paid, rangeStart,
                    rangeEnd, onlyAvailable, pageRequest).toList());
        }
        List<EventResponseDto> eventResponseDtos = new ArrayList<>();
        for (Event event : events) {
            User user = userRepository.findById(event.getInitiatorId()).get();
            Category category = categoryRepository.findById(event.getCategoryId()).get();
            Location location = locationRepository.findById(event.getLocationId()).get();
            eventResponseDtos.add(toEventResponseDto(event, user, category, location, eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(event.getId())));
        }
        log.debug("Предоставлены данные по событиям");
        if (sort == SortEvent.EVENT_DATE) {
            eventResponseDtos.stream().sorted(new Comparator<EventResponseDto>() {
                        @Override
                        public int compare(EventResponseDto o1, EventResponseDto o2) {
                            if (o1.getEventDate().isBefore(o2.getEventDate())) {
                                return 1;
                            } else if (o2.getEventDate().isAfter(o2.getEventDate())) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (sort == SortEvent.VIEWS) {
            eventResponseDtos.stream().sorted(new Comparator<EventResponseDto>() {
                        @Override
                        public int compare(EventResponseDto o1, EventResponseDto o2) {
                            if (o1.getViews() > o2.getViews()) {
                                return 1;
                            } else if (o2.getViews() < o2.getViews()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    })
                    .collect(Collectors.toList());
        }
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
        return toEventResponseDto(event, user, category, location, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId()));
    }

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new ObjectNotFoundException("Событие ID: " + eventId + ", не найдено!");
        }
    }
}
