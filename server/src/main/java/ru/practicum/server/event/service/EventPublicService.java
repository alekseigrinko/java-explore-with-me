package ru.practicum.server.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.SortEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {

    List<EventFullDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort, PageRequest pageRequest);

    EventFullDto getEvent(long eventId);
}
