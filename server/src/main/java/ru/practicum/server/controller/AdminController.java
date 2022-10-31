package ru.practicum.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;
import ru.practicum.server.category.service.CategoryAdminService;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.service.CompilationAdminService;
import ru.practicum.server.event.dto.AdminUpdateEventRequest;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.service.EventAdminService;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.service.UserAdminService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "admin")
public class AdminController {

    private final CategoryAdminService categoryAdminService;
    private final UserAdminService userAdminService;
    private final CompilationAdminService compilationAdminService;
    private final EventAdminService eventAdminService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdminController(CategoryAdminService categoryAdminService, UserAdminService userAdminService,
                           CompilationAdminService compilationAdminService, EventAdminService eventAdminService) {
        this.categoryAdminService = categoryAdminService;
        this.userAdminService = userAdminService;
        this.compilationAdminService = compilationAdminService;
        this.eventAdminService = eventAdminService;
    }

    @PostMapping("/categories")
    CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryAdminService.addCategory(newCategoryDto);
    }


    @PatchMapping("/categories")
    CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.debug("Переданы данные для регистрации категории");
        return categoryAdminService.updateCategory(categoryDto);
    }

    @DeleteMapping("/categories/{id}")
    CategoryDto deleteCategory(@PathVariable long id) {
        return categoryAdminService.deleteCategory(id);
    }

    @PostMapping("/users")
    UserDto createUser(@RequestBody NewUserRequest newUserRequest) {
        return userAdminService.addUser(newUserRequest);
    }

    @GetMapping("/users")
    List<UserDto> getAllUsers(@RequestParam(value = "from", defaultValue = "0") int from,
                              @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userAdminService.getAllUsers(pageRequest);
    }

    @GetMapping("/users/{ids}")
    Page<UserDto> getUsers(@PathVariable List<Long> ids,
                           @RequestParam(value = "from", defaultValue = "0") int from,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userAdminService.getUsers(ids, pageRequest);
    }

    @DeleteMapping("/users/{userId}")
    UserDto deleteUser(@PathVariable long userId) {
        return userAdminService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    CompilationDto postCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.postCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{complId}")
    void deleteCompilation(@PathVariable long complId) {
        compilationAdminService.deleteCompilation(complId);
    }

    @DeleteMapping("/compilations/{complId}/events/{eventId}")
    void deleteEventFromCompilation(@PathVariable long complId,
                                    @PathVariable long eventId) {
        compilationAdminService.deleteEventFromCompilation(complId, eventId);
    }

    @PatchMapping("/compilations/{complId}/events/{eventId}")
    void putEventFromCompilation(@PathVariable long complId,
                                 @PathVariable long eventId) {
        compilationAdminService.putEventToCompilation(complId, eventId);
    }

    @DeleteMapping("/compilations/{complId}/pin")
    void unpinnedCompilation(@PathVariable long complId) {
        compilationAdminService.unpinnedCompilation(complId);
    }

    @PatchMapping("/compilations/{complId}/pin")
    void pinnedCompilation(@PathVariable long complId) {
        compilationAdminService.pinnedCompilation(complId);
    }

    @GetMapping( "/events")
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

    @PutMapping("/events/{eventId}")
    EventFullDto updateEvent(@PathVariable long eventId,
                             @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventAdminService.updateEvent(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/events/{eventId}/publish")
    EventFullDto publishEvent(@PathVariable long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    EventFullDto rejectEvent(@PathVariable long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }
}
