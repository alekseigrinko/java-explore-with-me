package ru.practicum.server.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    UserDto createUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@RequestBody UserDto userDto,
                       @PathVariable long userId) {
        return userService.updateUser(userId, userDto);
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
