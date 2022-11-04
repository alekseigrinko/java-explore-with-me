package ru.practicum.server.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.AdminUpdateEventRequestDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.service.adm.EventAdminService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Контроллер администратора для работы с данными событий
 * */
@RestController
@Slf4j
@RequestMapping(path = "admin/events")
public class AdminEventController {

    /**
     * @see EventAdminService - интерефейс методов администратора
     * */
    private final EventAdminService eventAdminService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdminEventController(EventAdminService eventAdminService) {
        this.eventAdminService = eventAdminService;
    }

    /**
     * Получение списка событий по заданным параметрам
     * */
    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "states") List<String> states,
                                           @RequestParam(value = "categories") List<Integer> categories,
                                           @RequestParam(value = "users") List<Integer> users,
                                           @RequestParam(value = "rangeStart") String rangeStart,
                                           @RequestParam(value = "rangeEnd") String rangeEnd
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").descending());
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventAdminService.getAllEvents(states, categories, users, start, end, pageRequest);
    }

    /**
     * Обновление данных события
     * */
    @PutMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable long eventId,
                             @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        return eventAdminService.updateEvent(eventId, adminUpdateEventRequestDto);
    }

    /**
     * Публикация события
     * */
    @PatchMapping("/{eventId}/publish")
    EventFullDto publishEvent(@PathVariable long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    /**
     * Отмена публикации события
     * */
    @PatchMapping("/{eventId}/reject")
    EventFullDto rejectEvent(@PathVariable long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }
}
