package ru.practicum.server.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.SortEvent;
import ru.practicum.server.exeption.ApiError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.event.EventMapper.toEventFullDto;

@Service
@Slf4j
public class EventPublicServiceImp implements EventPublicService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventClient eventClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventPublicServiceImp(EventRepository eventRepository, CategoryRepository categoryRepository,
                                 UserRepository userRepository, RequestRepository requestRepository,
                                 EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    @Override
    public List<EventFullDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort, PageRequest pageRequest) {
        if (text.equals("0") || text == null) {
            text = "";
        }
        List<Event> events = new ArrayList<>();
        if (categories.size() != 0 || categories == null) {
            for (Integer categoryId : categories) {
                if (rangeStart != null) {
                    events.addAll(eventRepository.getPublicAllEvents(text, categoryId.longValue(), paid, rangeStart,
                            rangeEnd, onlyAvailable, pageRequest).toList());
                } else {
                    events.addAll(eventRepository.getPublicAllEventsWithoutRange(text, categoryId.longValue(), paid,
                            LocalDateTime.now(), onlyAvailable, pageRequest).toList());
                }
            }
        } else {
            if (rangeStart != null) {
                events.addAll(eventRepository.getPublicAllEventsWithoutCategory(text, paid, rangeStart,
                        rangeEnd, onlyAvailable, pageRequest).toList());
            } else {
                events.addAll(eventRepository.getPublicAllEventsWithoutCategoryAndRange(text, paid,
                        LocalDateTime.now(), onlyAvailable, pageRequest).toList());
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
        if (sort == SortEvent.EVENT_DATE) {
            eventFullDtos.stream().sorted(new Comparator<EventFullDto>() {
                        @Override
                        public int compare(EventFullDto o1, EventFullDto o2) {
                            if (LocalDateTime.parse(o1.getEventDate(), formatter)
                                    .isBefore(LocalDateTime.parse(o2.getEventDate(), formatter))) {
                                return 1;
                            } else if (LocalDateTime.parse(o1.getEventDate(), formatter)
                                    .isAfter(LocalDateTime.parse(o2.getEventDate(), formatter))) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (sort == SortEvent.VIEWS) {
            eventFullDtos.stream().sorted(new Comparator<EventFullDto>() {
                        @Override
                        public int compare(EventFullDto o1, EventFullDto o2) {
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
        return eventFullDtos;
    }

    @Override
    public EventFullDto getEvent(long eventId) {
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        log.debug("Предоставлены данные по событию ID: " + eventId);
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId()));
    }

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new ApiError();
        }
    }
}
