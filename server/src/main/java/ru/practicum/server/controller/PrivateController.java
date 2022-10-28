package ru.practicum.server.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.service.EventPrivateService;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.service.RequestPrivateService;

import java.util.List;

@RestController
@RequestMapping(path = "user")
public class PrivateController {

    final private RequestPrivateService requestPrivateService;
    final private EventPrivateService eventPrivateService;

    public PrivateController(RequestPrivateService requestPrivateService, EventPrivateService eventPrivateService) {
        this.requestPrivateService = requestPrivateService;
        this.eventPrivateService = eventPrivateService;
    }

    @PostMapping("/{userId}/requests")
    RequestDto postRequest(@PathVariable long userId,
                           @RequestParam long eventId) {
        return requestPrivateService.postRequest(userId, eventId);
    }


    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    RequestDto updateCategory(@PathVariable long userId,
                              @PathVariable long requestId) {
        return requestPrivateService.canceledRequest(requestId, userId);
    }

    @GetMapping("/{userId}/requests/")
    List<RequestDto> getUserRequest(@PathVariable long userId) {
        return requestPrivateService.getAllUsersRequests(userId);
    }

    @PostMapping("/{userId}/events")
    EventResponseDto postEvent(@PathVariable long userId,
                               @RequestBody EventDto eventDto) {
        return eventPrivateService.addEvent(userId, eventDto);
    }


    @GetMapping("/{userId}/events")
    List<EventResponseDto> getUserEvents(@PathVariable long userId,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        return eventPrivateService.getAllUserEvents(userId, pageRequest);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventResponseDto getUserEvents(@PathVariable long userId,
                                   @PathVariable long eventId) {
        return eventPrivateService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventResponseDto canselEvent(@PathVariable long userId,
                                 @PathVariable long eventId) {
        return eventPrivateService.cancelEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
        // обратить внимание
    EventResponseDto canselEvent(@PathVariable long userId, @RequestBody EventDto eventDto) {
        return eventPrivateService.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<RequestDto> getRequestsForEventByUser(@PathVariable long userId,
                                               @PathVariable long eventId) {
        return requestPrivateService.getRequestsForEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    RequestDto confirmRequest(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestPrivateService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    RequestDto rejectRequest(@PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestPrivateService.rejectRequest(userId, eventId, reqId);
    }
}
