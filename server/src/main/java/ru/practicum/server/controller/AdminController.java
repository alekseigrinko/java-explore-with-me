package ru.practicum.server.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.service.CategoryAdminService;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.CompilationResponseDto;
import ru.practicum.server.compilation.service.CompilationAdminService;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.service.EventAdminService;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.service.UserAdminService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    private final CategoryAdminService categoryAdminService;
    private final UserAdminService userAdminService;
    private final CompilationAdminService compilationAdminService;
    private final EventAdminService eventAdminService;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdminController(CategoryAdminService categoryAdminService, UserAdminService userAdminService,
                           CompilationAdminService compilationAdminService, EventAdminService eventAdminService) {
        this.categoryAdminService = categoryAdminService;
        this.userAdminService = userAdminService;
        this.compilationAdminService = compilationAdminService;
        this.eventAdminService = eventAdminService;
    }

    @PostMapping("/categories")
    CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryAdminService.addCategory(categoryDto);
    }


    @PatchMapping("/categories")
    CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        return categoryAdminService.updateCategory(categoryDto);
    }

    @DeleteMapping("/categories/{id}")
    CategoryDto deleteCategory(@PathVariable long id) {
        return categoryAdminService.deleteCategory(id);
    }

    @PostMapping("/users")
    UserDto createUser(@RequestBody UserDto userDto) {
        return userAdminService.addUser(userDto);
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
    CompilationResponseDto postCompilation(@RequestBody CompilationDto compilationDto) {
        return compilationAdminService.postCompilation(compilationDto);
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
    public List<EventResponseDto> getAllEvents(@RequestParam(value = "from", defaultValue = "0") int from,
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
    EventResponseDto updateEvent(@PathVariable long eventId,
                                 @RequestBody EventDto eventDto) {
        return eventAdminService.updateEvent(eventId, eventDto);
    }

    @PatchMapping("/events/{eventId}/publish")
    EventResponseDto publishEvent(@PathVariable long eventId) {
        return eventAdminService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    EventResponseDto rejectEvent(@PathVariable long eventId) {
        return eventAdminService.rejectEvent(eventId);
    }
}
