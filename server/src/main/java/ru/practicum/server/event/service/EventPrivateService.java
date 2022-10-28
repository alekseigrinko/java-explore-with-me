package ru.practicum.server.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;

import java.util.List;

public interface EventPrivateService {

    List<EventResponseDto> getAllUserEvents(long userId, PageRequest pageRequest);
    EventResponseDto getUserEventById(long userId, long eventId);

    EventResponseDto addEvent(long userId, EventDto eventDto);

    EventResponseDto updateEvent(long userId, long eventId, EventDto eventDto);

    EventResponseDto cancelEvent(long userId, long eventId);
}
