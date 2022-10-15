package ru.practicum.server.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "admin/users")
public class AdminUserController {

    UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    UserDto createUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping
    List<UserDto> getAllUsers( @RequestParam(value = "from", defaultValue = "0") int from,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        return userService.getAllUsers(pageRequest);
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    UserDto deleteUser(@PathVariable long userId) {
        return userService.deleteUser(userId);
    }
}
