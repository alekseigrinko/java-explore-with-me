package ru.practicum.server.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.UpdateEventRequest;

import java.util.List;

public interface EventPrivateService {

    List<EventFullDto> getAllUserEvents(long userId, PageRequest pageRequest);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto addEvent(long userId, NewEventDto newEventDto);

    EventFullDto updateEvent(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto cancelEvent(long userId, long eventId);
}
