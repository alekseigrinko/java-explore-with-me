package ru.practicum.server.event.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.HitDto;
import ru.practicum.server.event.model.PublicEventFilter;
import ru.practicum.server.event.model.SortEvent;
import ru.practicum.server.event.service.publ.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Публичный контроллер для работы с данными событий
 * */
@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "events")
public class PublicEventController {

    /**
     * @see EventPublicService - интерефейс публичных методов
     * */
    EventPublicService eventPublicService;

    /**
     * Клиент для взаимодействия с сервисом статистики
     * @see EventClient
     * */
    EventClient eventClient;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PublicEventController(EventPublicService eventPublicService, EventClient eventClient) {
        this.eventPublicService = eventPublicService;
        this.eventClient = eventClient;
    }

    /**
     * Получение списка событий по заданным параметрам
     * Направление данных запроса на сервер статистики
     * @see EventClient
     * */
    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "text") String text,
                                           @RequestParam(value = "categories") List<Integer> categories,
                                           @RequestParam(value = "paid") boolean paid,
                                           @RequestParam(value = "rangeStart", defaultValue = "") String rangeStart,
                                           @RequestParam(value = "rangeEnd", defaultValue = "") String rangeEnd,
                                           @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                           @RequestParam(value = "sort", defaultValue = "EVENT_DATE") SortEvent sort,
                                           HttpServletRequest request
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        eventClient.postHit(new HitDto(null, "ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter)));
        log.debug("Получен запрос на получение списка событий по заданным параметрам");
        return eventPublicService.getAllEvents(new PublicEventFilter(
                text,
                categories,
                paid,
                rangeStart.isEmpty() ? null : rangeStart,
                rangeEnd.isEmpty() ? null : rangeEnd
        ), onlyAvailable, sort, pageRequest);
    }

    /**
     * Получение данных события по его ID
     * Направление данных запроса на сервер статистики
     * @see EventClient
     * */
    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        eventClient.postHit(new HitDto(null, "ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter)));
        log.debug("Получен запрос на получение данных события по его ID");
        return eventPublicService.getEvent(id);
    }
}
