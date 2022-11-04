package ru.practicum.server.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.UpdateEventRequestDto;
import ru.practicum.server.event.service.pvt.EventPrivateService;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.service.RequestPrivateService;

import java.util.List;

/**
 * Контроллер пользователя для работы с данными событий
 * */
@RestController
@Slf4j
@RequestMapping(path = "users/{userId}/events")
public class PrivateEventController {

    /**
     * @see RequestPrivateService - интерефейс методов пользователя по работе с запросами на участие в событиях
     * */
    private final RequestPrivateService requestPrivateService;

    /**
     * @see EventPrivateService - интерефейс методов пользователя по работе с событиями
     * */
    private final EventPrivateService eventPrivateService;

    public PrivateEventController(RequestPrivateService requestPrivateService, EventPrivateService eventPrivateService) {
        this.requestPrivateService = requestPrivateService;
        this.eventPrivateService = eventPrivateService;
    }

    /**
     * Размещение данных нового события
     * */
    @PostMapping
    EventFullDto postEvent(@PathVariable long userId,
                           @RequestBody NewEventDto newEventDto) {
        log.debug("Получен запрос на добавления события");
        return eventPrivateService.addEvent(userId, newEventDto);
    }

    /**
     * Предоставление списка всех событий, размещенных пользователем
     * */
    @GetMapping
    List<EventFullDto> getUserEvents(@PathVariable long userId,
                                     @RequestParam(value = "from", defaultValue = "0") int from,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        return eventPrivateService.getAllUserEvents(userId, pageRequest);
    }

    /**
     * Получение данных события по его ID
     * */
    @GetMapping("/{eventId}")
    EventFullDto getUserEvents(@PathVariable long userId,
                               @PathVariable long eventId) {
        return eventPrivateService.getUserEventById(userId, eventId);
    }

    /**
     * Отмена пользователем события
     * */
    @PatchMapping("/{eventId}")
    EventFullDto canselEvent(@PathVariable long userId,
                             @PathVariable long eventId) {
        return eventPrivateService.cancelEvent(userId, eventId);
    }

    /**
     * Обновление пользователем данных события
     * */
    @PatchMapping
    EventFullDto updateEvent(@PathVariable long userId, @RequestBody UpdateEventRequestDto updateEventRequestDto) {
       return eventPrivateService.updateEvent(userId, updateEventRequestDto);
    }

    /**
     * Получение данных запросов на участие в событии по его ID
     * */
    @GetMapping("/{eventId}/requests")
    List<ParticipationRequestDto> getRequestsForEventByUser(@PathVariable long userId,
                                                            @PathVariable long eventId) {
        return requestPrivateService.getRequestsForEventByUser(userId, eventId);
    }

    /**
     * Подтверждение запроса на участие в событии
     * */
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    ParticipationRequestDto confirmRequest(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestPrivateService.confirmRequest(userId, eventId, reqId);
    }

    /**
     * Отклонение запроса на участие в событии
     * */
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    ParticipationRequestDto rejectRequest(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestPrivateService.rejectRequest(userId, eventId, reqId);
    }
}
