package ru.practicum.server.event.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.dto.AdminUpdateEventRequestDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.AdminEventFilter;
import ru.practicum.server.event.service.adm.EventAdminService;

import java.util.List;

/**
 * Контроллер администратора для работы с данными событий
 */
@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "admin/events")
public class AdminEventController {

    /**
     * @see EventAdminService - интерефейс методов администратора
     */
    EventAdminService eventAdminService;

    public AdminEventController(EventAdminService eventAdminService) {
        this.eventAdminService = eventAdminService;
    }

    /**
     * Получение списка событий по заданным параметрам
     */
    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "states", defaultValue = "") List<String> states,
                                           @RequestParam(value = "categories") List<Integer> categories,
                                           @RequestParam(value = "users") List<Integer> users,
                                           @RequestParam(value = "rangeStart", defaultValue = "") String rangeStart,
                                           @RequestParam(value = "rangeEnd", defaultValue = "") String rangeEnd
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").descending());
        log.debug("Получен запрос на получение списка событий по заданным параметрам");
        return eventAdminService.getAllEvents(new AdminEventFilter(
                states.size() == 0 ? null : states,
                categories,
                users,
                rangeStart.isEmpty() ? null : rangeStart,
                rangeEnd.isEmpty() ? null : rangeEnd),
                pageRequest);
    }

    /**
     * Обновление данных события
     */
    @PutMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable long eventId,
                             @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        log.debug("Получен запрос на обновление данных события");
        return eventAdminService.updateEvent(eventId, adminUpdateEventRequestDto);
    }

    /**
     * Публикация события
     */
    @PatchMapping("/{eventId}/publish")
    EventFullDto publishEvent(@PathVariable long eventId) {
        log.debug("Получен запрос на публикацию события");
        return eventAdminService.publishEvent(eventId);
    }

    /**
     * Отмена публикации события
     */
    @PatchMapping("/{eventId}/reject")
    EventFullDto rejectEvent(@PathVariable long eventId) {
        log.debug("Получен запрос на отмену публикации события");
        return eventAdminService.rejectEvent(eventId);
    }
}
