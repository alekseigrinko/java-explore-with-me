package ru.practicum.server.request.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestError;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestMapper;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.Status;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.request.RequestMapper.toRequest;
import static ru.practicum.server.request.RequestMapper.toParticipationRequestDto;

/**
 * реализация интерфейса пользователя по работе с данными запросов ан участие в событиях
 * @see RequestPrivateService
 * */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestPrivateServiceImp implements RequestPrivateService {

    /**
     * репозиторий пользователей
     * @see UserRepository
     * */
    UserRepository userRepository;

    /**
     * репозиторий запросов на участие в событиях
     * @see RequestRepository
     * */
    RequestRepository requestRepository;

    /**
     * репозиторий событий
     * @see EventRepository
     * */
    EventRepository eventRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RequestPrivateServiceImp(UserRepository userRepository, RequestRepository requestRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * @see RequestPrivateService#postRequest(long, long)
     * */
    @Override
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        checkUser(eventId);
        checkEventForRequest(eventId, userId);
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto(
                LocalDateTime.now().format(formatter),
                eventId,
                null,
                userId,
                checkStatusRequest(eventId).toString()
        );
        log.debug("Добавлен запрос на участие в событии ID: " + eventId);
        return toParticipationRequestDto(requestRepository.save(toRequest(participationRequestDto)));
    }

    /**
     * @see RequestPrivateService#canceledRequest(long, long)
     * */
    @Override
    public ParticipationRequestDto canceledRequest(long requestId, long userId) {
        checkUser(userId);
        Request request = returnRequestWithCheck(requestId);
        if (request.getStatus() == Status.CONFIRMED) {
            Event event = eventRepository.findById(request.getEvent()).get();
            event.setLimit(false);
        }
        request.setStatus(Status.CANCELED);
        log.debug("Отменен запрос ID: " + requestId);
        return toParticipationRequestDto(requestRepository.save(request));
    }

    /**
     * @see RequestPrivateService#getAllUsersRequests(long)
     * */
    @Override
    public List<ParticipationRequestDto> getAllUsersRequests(long userId) {
        checkUser(userId);
        log.debug("Предоставлен список запросов на участие в событиях пользователя ID: " + userId);
        return requestRepository.getAllByRequester(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * @see RequestPrivateService#getRequestsForEventByUser(long, long)
     * */
    @Override
    public List<ParticipationRequestDto> getRequestsForEventByUser(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        if (userId != eventRepository.findById(eventId).get().getInitiatorId()) {
            log.warn("Пользователь не является инициатором события");
            throw new RuntimeException("Пользователь не является инициатором события");
        }
        List<Request> requests = requestRepository.findAllByEvent(eventId);
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();
        for (Request request : requests) {
            participationRequestDtoList.add(toParticipationRequestDto(request));
        }
        log.debug("Предоставлен список запросов на участие в событии ID: " + eventId + " пользователя ID: " + userId);
        return participationRequestDtoList;
    }

    /**
     * @see RequestPrivateService#confirmRequest(long, long, long)
     * */
    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId) {
        checkUser(userId);
        checkEvent(eventId);
        Request request = returnRequestWithCheck(requestId);
        request.setStatus(Status.CONFIRMED);
        log.debug("Подтвержден запрос ID: " + requestId);
        Event event = eventRepository.findById(eventId).get();
        event.setLimit(true);
        return toParticipationRequestDto(requestRepository.save(request));
    }

    /**
     * @see RequestPrivateService#rejectRequest(long, long, long)
     * */
    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId) {
        checkUser(userId);
        checkEvent(eventId);
        Request request = returnRequestWithCheck(requestId);
        if (request.getStatus() == Status.CONFIRMED) {
            Event event = eventRepository.findById(eventId).get();
            event.setLimit(false);
        }
        request.setStatus(Status.REJECTED);
        log.debug("Отклонен запрос ID: " + requestId);
        return toParticipationRequestDto(requestRepository.save(request));
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
     * метод проверки наличия запроса на участие в событии по ID в репозитории и его возвращении
     * @return запрос на участие в событии по ID
     * @throws NotFoundError - при отсутствии запроса по ID
     * */
    public Request returnRequestWithCheck(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            log.warn("Запрос ID: " + requestId + ", не найден!");
            throw new NotFoundError("Запрос ID: " + requestId + ", не найден!");
        });
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
     * метод проверки размещения запроса на участие в событии
     * @throws BadRequestError - при нарушении условий подачи запроса инициатором события,
     * подачи запроса на неопубликованное событие, события достигнут лимит участников
     * */
    public void checkEventForRequest(long eventId, long userId) {
        if (eventRepository.findById(eventId).get().getInitiatorId() == userId) {
            log.warn("Инициатор события не может подавать запрос на участие в нем!");
            throw new BadRequestError("Инициатор события не может подавать запрос на участие в нем!");
        }
        if (eventRepository.findById(eventId).get().getState() != State.PUBLISHED) {
            log.warn("Запрос не может быть подан на не опубликованное событие!");
            throw new BadRequestError("Запрос не может быть подан на не опубликованное событие!");
        }
        if (eventRepository.findById(eventId).get().getParticipantLimit() == requestRepository.getEventParticipantLimit(eventId)) {
            log.warn("Достигнут лимит участников в событии!");
            throw new BadRequestError("Достигнут лимит участников в событии!");
        }
    }

    /**
     * метод проверки статуса запроса
     * */
    public Status checkStatusRequest(long eventId) {
        if (!eventRepository.findById(eventId).get().isRequestModeration()) {
            checkMaxRequest(eventId);
            return Status.CONFIRMED;
        }
        return Status.PENDING;
    }

    /**
     * метод проверки лимита участников события
     * @throws BadRequestError - у события достигнут лимит участников
     * */
    public void checkMaxRequest(long eventId) {
        if (requestRepository.getEventParticipantLimit(eventId) == eventRepository.findById(eventId).get()
                .getParticipantLimit()) {
            log.warn("Достигнут лимит участников в событии!");
            throw new BadRequestError("Достигнут лимит участников в событии!");
        }
    }
}
