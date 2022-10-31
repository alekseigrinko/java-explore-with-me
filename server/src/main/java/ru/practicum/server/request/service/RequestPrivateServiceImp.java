package ru.practicum.server.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.request.RequestMapper;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.Status;
import ru.practicum.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.request.RequestMapper.toRequest;
import static ru.practicum.server.request.RequestMapper.toRequestDto;

@Service
@Slf4j
public class RequestPrivateServiceImp implements RequestPrivateService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

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
                null,
                userId,
                eventId,
                LocalDateTime.now(),
                checkStatusRequest(eventId)
        );
        log.debug("Добавлен запрос на участие в событии ID: " + eventId);
        return toRequestDto(requestRepository.save(toRequest(participationRequestDto)));
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
        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllUsersRequests(long userId) {
        checkUser(userId);
        log.debug("Предоставлен список запросов на участие в событиях пользователя ID: " + userId);
        return requestRepository.getAllByRequester(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForEventByUser(long userId, long eventId) {
        checkUser(userId);
        checkRequest(eventId);
        log.debug("Предоставлен список запросов на участие в событии ID: " + eventId + " пользователя ID: " + userId);
        return requestRepository.getAllByRequesterAndAndEvent(userId, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId) {
        checkUser(userId);
        checkRequest(requestId);
        Request request = requestRepository.findById(requestId).get();
        checkRequest(request.getEvent());
        request.setStatus(Status.CONFIRMED);
        log.debug("Подтвержден запрос ID: " + requestId);
        Event event = eventRepository.findById(eventId).get();
        event.setLimit(true);
        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId) {
        checkUser(userId);
        checkRequest(requestId);
        Request request = requestRepository.findById(requestId).get();
        if (request.getStatus() == Status.CONFIRMED) {
            Event event = eventRepository.findById(eventId).get();
            event.setLimit(false);
        }
        request.setStatus(Status.REJECTED);
        log.debug("Отклонен запрос ID: " + requestId);
        return toRequestDto(requestRepository.save(request));
    }


    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }

    public void checkRequest(long requestId) {
        if (!requestRepository.existsById(requestId)) {
            log.warn("Запрос ID: " + requestId + ", не найден!");
            throw new ObjectNotFoundException("Запрос ID: " + requestId + ", не найден!");
        }
    }

    public void checkEventForRequest(long eventId, long userId) {
        if (eventRepository.findById(eventId).get().getInitiatorId() == userId) {
            log.warn("Инициатор события не может подавать запрос на участие в нем!");
            throw new BadRequestException("Инициатор события не может подавать запрос на участие в нем!");
        }
        if (eventRepository.findById(eventId).get().getState() != State.PUBLISHED) {
            log.warn("Запрос не может быть подан на не опубликованное событие!");
            throw new BadRequestException("Запрос не может быть подан на не опубликованное событие!");
        }
        if (eventRepository.findById(eventId).get().getParticipantLimit() == requestRepository.getEventParticipantLimit(eventId)) {
            log.warn("Достигнут лимит участников в событии!");
            throw new BadRequestException("Достигнут лимит участников в событии!");
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
            throw new BadRequestException("Достигнут лимит участников в событии!");
        }
    }
}
