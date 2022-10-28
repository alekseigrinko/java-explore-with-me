package ru.practicum.server.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    List<EventResponseDto> getAllEvents(List<String> states,List<Integer> categories, List<Integer> users, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, PageRequest pageRequest);

    EventResponseDto updateEvent(long eventId, EventDto eventDto);

    EventResponseDto publishEvent(long eventId);

    EventResponseDto rejectEvent(long eventId);
}
