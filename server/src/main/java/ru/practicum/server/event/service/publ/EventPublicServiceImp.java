package ru.practicum.server.event.service.publ;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.QEvent;
import ru.practicum.server.event.model.SortEvent;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.event.EventMapper.toEventFullDto;

/**
 * реализация публичного интерфейса по работе с данными событий
 * @see EventPublicService
 * */
@Service
@Slf4j
public class EventPublicServiceImp implements EventPublicService {

    /**
     * репозиторий событий
     * @see EventRepository
     * */
    private final EventRepository eventRepository;

    /**
     * репозиторий категорий
     * @see CategoryRepository
     * */
    private final CategoryRepository categoryRepository;

    /**
     * репозиторий пользователей
     * @see UserRepository
     * */
    private final UserRepository userRepository;

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
     * @see EventPublicService#getAllEvents(String, List, boolean, LocalDateTime, LocalDateTime, boolean, SortEvent, PageRequest)
     * @param text - не должен быть null
     * @param categories - не должен быть null
     * @param sort - не должен быть null
     * */
    @Override
    public List<EventFullDto> getAllEvents(@NotBlank String text, @NonNull List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, boolean onlyAvailable, @NonNull SortEvent sort, PageRequest pageRequest) {
        Iterable<Event> events = eventRepository.findAll(formatExpression(text, categories, paid, rangeStart,
                rangeEnd), pageRequest);
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
                            } else if (o2.getViews() < o2.getViews()) {
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
     * @see EventPublicService#getAllEvents(String, List, boolean, LocalDateTime, LocalDateTime, boolean, SortEvent, PageRequest)
     * @param text - не должен быть null
     * @param paid - не должен быть null
     * */
    private BooleanExpression formatExpression(@NotBlank String text, List<Integer> categoriesList, @Nullable boolean paid, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd) {
        BooleanExpression result = QEvent.event.state.eq(State.PUBLISHED);
        result = result.and(QEvent.event.description.likeIgnoreCase(text)
                .or(QEvent.event.annotation.likeIgnoreCase(text)));

        if (categoriesList != null) {
            List<Long> categories = new ArrayList<>();
            for (Integer i : categoriesList) {
                categories.add(i.longValue());
            }
            result = result.and(QEvent.event.categoryId.in(categories));
        }
        result = result.and(QEvent.event.paid.eq(paid)); // уточнить
        if (rangeStart != null && rangeEnd != null) {
            result = result.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (rangeStart != null) {
            result = result.and(QEvent.event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            result = result.and(QEvent.event.eventDate.before(rangeEnd));
        }
        return result;
    }

    /**
     * @see EventPublicService#getEvent(long)
     * */
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
}
