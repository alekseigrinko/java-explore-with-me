package ru.practicum.server.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.AdminUpdateEventRequest;
import ru.practicum.server.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    List<EventFullDto> getAllEvents(List<String> states, List<Integer> categories, List<Integer> users, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, PageRequest pageRequest);

    EventFullDto updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);
}
