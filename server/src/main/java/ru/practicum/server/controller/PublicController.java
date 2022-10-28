package ru.practicum.server.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.service.CategoryPublicService;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.service.EventPublicService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class PublicController {

    EventClient eventClient;
    EventPublicService eventPublicService;
    CategoryPublicService categoryPublicService;

    public PublicController(EventPublicService eventPublicService, CategoryPublicService categoryPublicService,
                            EventClient eventClient) {
        this.eventPublicService = eventPublicService;
        this.categoryPublicService = categoryPublicService;
        this.eventClient = eventClient;
    }

    @GetMapping(path = "categories")
    List<CategoryDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") int from,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        return categoryPublicService.getAllCategories(pageRequest);
    }

    @GetMapping(path = "categories/{id}")
    CategoryDto getCategory(@PathVariable long id) {
        return categoryPublicService.getCategory(id);
    }

    @GetMapping(path = "events")
    public List<EventResponseDto> getAllEvents(@RequestParam(value = "from", defaultValue = "0") int from,
                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                               @RequestParam(value = "text") String text,
                                               @RequestParam(value = "categories") List<Integer> categories,
                                               @RequestParam(value = "paid") boolean paid,
                                               @RequestParam(value = "rangeStart") LocalDateTime rangeStart,
                                               @RequestParam(value = "rangeEnd") LocalDateTime rangeEnd,
                                               @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                               @RequestParam(value = "sort", defaultValue = "EVENT_DATE, VIEWS") String sort
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("eventDate").descending());
        return eventPublicService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageRequest);
    }

    @GetMapping(path = "events/{id}")
    public EventResponseDto getEvent(@PathVariable long id) {
        return eventPublicService.getEvent(id);
    }
}
