package ru.practicum.server.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.ApiError;
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

@Service
@Slf4j
public class RequestPrivateServiceImp implements RequestPrivateService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RequestPrivateServiceImp(UserRepository userRepository, RequestRepository requestRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }

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

    @Override
    public ParticipationRequestDto canceledRequest(long requestId, long userId) {
        checkUser(userId);
        checkRequest(requestId);
        Request request = requestRepository.findById(requestId).get();
        if (request.getStatus() == Status.CONFIRMED) {
            Event event = eventRepository.findById(request.getEvent()).get();
            event.setLimit(false);
        }
        request.setStatus(Status.CANCELED);
        log.debug("Отменен запрос ID: " + requestId);
        return toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllUsersRequests(long userId) {
        checkUser(userId);
        log.debug("Предоставлен список запросов на участие в событиях пользователя ID: " + userId);
        return requestRepository.getAllByRequester(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForEventByUser(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        List<Request> requests = requestRepository.getAllRequestsByUserForEvent(userId, eventId);
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();
        for (Request request : requests) {
            participationRequestDtoList.add(toParticipationRequestDto(request));
        }
        log.debug("Предоставлен список запросов на участие в событии ID: " + eventId + " пользователя ID: " + userId);
        return participationRequestDtoList;
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId) {
        checkUser(userId);
        checkEvent(eventId);
        checkRequest(requestId);
        Request request = requestRepository.findById(requestId).get();
        request.setStatus(Status.CONFIRMED);
        log.debug("Подтвержден запрос ID: " + requestId);
        Event event = eventRepository.findById(eventId).get();
        event.setLimit(true);
        return toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId) {
        checkUser(userId);
        checkEvent(eventId);
        checkRequest(requestId);
        Request request = requestRepository.findById(requestId).get();
        if (request.getStatus() == Status.CONFIRMED) {
            Event event = eventRepository.findById(eventId).get();
            event.setLimit(false);
        }
        request.setStatus(Status.REJECTED);
        log.debug("Отклонен запрос ID: " + requestId);
        return toParticipationRequestDto(requestRepository.save(request));
    }


    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ApiError();
        }
    }

    public void checkRequest(long requestId) {
        if (!requestRepository.existsById(requestId)) {
            log.warn("Запрос ID: " + requestId + ", не найден!");
            throw new ApiError();
        }
    }

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new ApiError();
        }
    }

    public void checkEventForRequest(long eventId, long userId) {
        if (eventRepository.findById(eventId).get().getInitiatorId() == userId) {
            log.warn("Инициатор события не может подавать запрос на участие в нем!");
            throw new ApiError();
        }
        if (eventRepository.findById(eventId).get().getState() != State.PUBLISHED) {
            log.warn("Запрос не может быть подан на не опубликованное событие!");
            throw new ApiError();
        }
        if (eventRepository.findById(eventId).get().getParticipantLimit() == requestRepository.getEventParticipantLimit(eventId)) {
            log.warn("Достигнут лимит участников в событии!");
            throw new ApiError();
        }
    }

    public Status checkStatusRequest(long eventId) {
        if (!eventRepository.findById(eventId).get().isRequestModeration()) {
            checkMaxRequest(eventId);
            return Status.CONFIRMED;
        }
        return Status.PENDING;
    }

    public void checkMaxRequest(long eventId) {
        if (requestRepository.getEventParticipantLimit(eventId) == eventRepository.findById(eventId).get()
                .getParticipantLimit()) {
            log.warn("Достигнут лимит участников в событии!");
            throw new ApiError();
        }
    }
}
