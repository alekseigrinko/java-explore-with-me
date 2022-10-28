package ru.practicum.server.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    List<EventResponseDto> getAllEvents(String text,List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, boolean onlyAvailable, PageRequest pageRequest);

    EventResponseDto updateEvent(long eventId, EventDto eventDto);

    EventResponseDto publishEvent(long eventId);

    EventResponseDto rejectEvent(long eventId);
}
