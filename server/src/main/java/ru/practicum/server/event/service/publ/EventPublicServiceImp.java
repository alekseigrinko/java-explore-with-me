package ru.practicum.server.event.service.publ;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.*;
import ru.practicum.server.exeption.NotFoundError;
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

/**
 * реализация публичного интерфейса по работе с данными событий
 *
 * @see EventPublicService
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventPublicServiceImp implements EventPublicService {

    /**
     * репозиторий событий
     * @see EventRepository
     */
    EventRepository eventRepository;

    /**
     * репозиторий категорий
     * @see CategoryRepository
     */
    CategoryRepository categoryRepository;

    /**
     * репозиторий пользователей
     * @see UserRepository
     */
    UserRepository userRepository;

    /**
     * репозиторий запросов на участие в событиях
     * @see RequestRepository
     */
    RequestRepository requestRepository;

    /**
     * клиент для взаимодействия с сервисом статистики
     * @see EventClient
     */
    EventClient eventClient;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventPublicServiceImp(EventRepository eventRepository, CategoryRepository categoryRepository,
                                 UserRepository userRepository, RequestRepository requestRepository,
                                 EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @param publicEventFilter - параметры запроса
     * @param sort       - не должен быть null
     * @see EventPublicService#getAllEvents(PublicEventFilter, boolean, SortEvent, PageRequest)
     */
    @Override
    public List<EventFullDto> getAllEvents(PublicEventFilter publicEventFilter, boolean onlyAvailable, @NonNull SortEvent sort, PageRequest pageRequest) {
        Iterable<Event> events = eventRepository.findAll(formatExpression(publicEventFilter), pageRequest);
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (Event event : events) {
            User user = userRepository.findById(event.getInitiatorId()).get();
            Category category = categoryRepository.findById(event.getCategoryId()).get();
            eventFullDtoList.add(toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(event.getId())));
        }
        if (sort == SortEvent.EVENT_DATE) {
            eventFullDtoList.stream()
                    .sorted(new Comparator<EventFullDto>() {
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
            eventFullDtoList.stream().sorted(new Comparator<EventFullDto>() {
                        @Override
                        public int compare(EventFullDto o1, EventFullDto o2) {
                            if (o1.getViews() > o2.getViews()) {
                                return 1;
                            } else if (o1.getViews() < o2.getViews()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    })
                    .collect(Collectors.toList());
        }

        for (EventFullDto eventFullDto : eventFullDtoList) {
            if (onlyAvailable) {
                if (requestRepository.getEventParticipantLimit(eventFullDto.getId()) != eventFullDto.getParticipantLimit()) {
                    eventFullDtoList.remove(eventFullDto);
                }
            } else {
                if (requestRepository.getEventParticipantLimit(eventFullDto.getId()) == eventFullDto.getParticipantLimit()) {
                    eventFullDtoList.remove(eventFullDto);
                }
            }
        }
        return eventFullDtoList;
    }

    /**
     * метод для определения фильтрации получения списка событий
     *
     * @param publicEventFilter - параметры запроса
     * @see EventPublicService#getAllEvents(PublicEventFilter, boolean, SortEvent, PageRequest)
     */
    private BooleanExpression formatExpression(PublicEventFilter publicEventFilter) {
        BooleanExpression result = QEvent.event.state.eq(State.PUBLISHED);
        result = result.and(QEvent.event.description.likeIgnoreCase(publicEventFilter.getText())
                .or(QEvent.event.annotation.likeIgnoreCase(publicEventFilter.getText())));

        if (publicEventFilter.getCategories() != null) {
            List<Long> categories = new ArrayList<>();
            for (Integer i : publicEventFilter.getCategories()) {
                categories.add(i.longValue());
            }
            result = result.and(QEvent.event.categoryId.in(categories));
        }
        result = result.and(QEvent.event.paid.eq(publicEventFilter.isPaid()));
        if (publicEventFilter.getRangeStart() != null && publicEventFilter.getRangeEnd() != null) {
            result = result.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (publicEventFilter.getRangeStart() != null) {
            result = result.and(QEvent.event.eventDate.after(LocalDateTime.parse(publicEventFilter.getRangeStart(), formatter)));
        }
        if (publicEventFilter.getRangeEnd() != null) {
            result = result.and(QEvent.event.eventDate.before(LocalDateTime.parse(publicEventFilter.getRangeEnd(), formatter)));
        }
        return result;
    }

    /**
     * @see EventPublicService#getEvent(long)
     * @throws NotFoundError - при отсутствии события по ID
     */
    @Override
    public EventFullDto getEvent(long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new NotFoundError("Событие ID: " + eventId + ", не найдено!");
        });
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        log.debug("Предоставлены данные по событию ID: " + eventId);
        return toEventFullDto(event, user, category, eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId()));
    }
}
