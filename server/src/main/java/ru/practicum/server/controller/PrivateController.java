package ru.practicum.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.UpdateEventRequest;
import ru.practicum.server.event.service.EventPrivateService;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.service.RequestPrivateService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "users")
public class PrivateController {

    private final RequestPrivateService requestPrivateService;
    private final EventPrivateService eventPrivateService;

    public PrivateController(RequestPrivateService requestPrivateService, EventPrivateService eventPrivateService) {
        this.requestPrivateService = requestPrivateService;
        this.eventPrivateService = eventPrivateService;
    }

    @PostMapping("/{userId}/requests")
    ParticipationRequestDto postRequest(@PathVariable long userId,
                                        @RequestParam long eventId) {
        return requestPrivateService.postRequest(userId, eventId);
    }


    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto updateCategory(@PathVariable long userId,
                                           @PathVariable long requestId) {
        return requestPrivateService.canceledRequest(requestId, userId);
    }

    @GetMapping("/{userId}/requests")
    List<ParticipationRequestDto> getUserRequest(@PathVariable long userId) {
        return requestPrivateService.getAllUsersRequests(userId);
    }

    @PostMapping("/{userId}/events")
    EventFullDto postEvent(@PathVariable long userId,
                           @RequestBody NewEventDto newEventDto) {
        log.debug("Получен запрос на добавления события");
        return eventPrivateService.addEvent(userId, newEventDto);
    }


    @GetMapping("/{userId}/events")
    List<EventFullDto> getUserEvents(@PathVariable long userId,
                                     @RequestParam(value = "from", defaultValue = "0") int from,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        return eventPrivateService.getAllUserEvents(userId, pageRequest);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventFullDto getUserEvents(@PathVariable long userId,
                               @PathVariable long eventId) {
        return eventPrivateService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventFullDto canselEvent(@PathVariable long userId,
                             @PathVariable long eventId) {
        return eventPrivateService.cancelEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
    EventFullDto updateEvent(@PathVariable long userId, @RequestBody UpdateEventRequest updateEventRequest) {
       return eventPrivateService.updateEvent(userId, updateEventRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getRequestsForEventByUser(@PathVariable long userId,
                                                            @PathVariable long eventId) {
        return requestPrivateService.getRequestsForEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    ParticipationRequestDto confirmRequest(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestPrivateService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    ParticipationRequestDto rejectRequest(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestPrivateService.rejectRequest(userId, eventId, reqId);
    }
}
