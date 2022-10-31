package ru.practicum.server.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.service.CategoryPublicService;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.service.CompilationPublicService;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.HitDto;
import ru.practicum.server.event.model.SortEvent;
import ru.practicum.server.event.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
public class PublicController {

    private final EventPublicService eventPublicService;
    private final CategoryPublicService categoryPublicService;
    private final CompilationPublicService compilationPublicService;
    private final EventClient eventClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PublicController(EventPublicService eventPublicService, CategoryPublicService categoryPublicService,
                            CompilationPublicService compilationPublicService, EventClient eventClient) {
        this.eventPublicService = eventPublicService;
        this.categoryPublicService = categoryPublicService;
        this.compilationPublicService = compilationPublicService;
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
    public List<EventFullDto> getAllEvents(@RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           @RequestParam(value = "text") String text,
                                           @RequestParam(value = "categories") List<Integer> categories,
                                           @RequestParam(value = "paid") boolean paid,
                                           @RequestParam(value = "rangeStart") String rangeStart,
                                           @RequestParam(value = "rangeEnd") String rangeEnd,
                                           @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                           @RequestParam(value = "sort") SortEvent sort,
                                           HttpServletRequest request
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        eventClient.postHit(new HitDto(null, "ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().toString()));
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventPublicService.getAllEvents(text, categories, paid, start, end, onlyAvailable, sort, pageRequest);
    }

    @GetMapping(path = "events/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        eventClient.postHit(new HitDto(null, "ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().toString()));
        return eventPublicService.getEvent(id);
    }

    @GetMapping(path = "compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        return compilationPublicService.getCompilation(compId);
    }

    @GetMapping(path = "compilations")
    List<CompilationDto> getAllCompilations(@RequestParam(value = "from", defaultValue = "0") int from,
                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        return compilationPublicService.getAllCompilations(pageRequest);
    }
}
