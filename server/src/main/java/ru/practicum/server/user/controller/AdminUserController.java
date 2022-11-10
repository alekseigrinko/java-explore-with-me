package ru.practicum.server.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.dto.NewUserRequestDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.service.UserAdminService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер администратора для работы с данными пользователей
 * */
@RestController
@Slf4j
@RequestMapping(path = "admin/users")
public class AdminUserController {

    /**
     * @see UserAdminService - интерефейс методов администратора
     * */
    private final UserAdminService userAdminService;

    public AdminUserController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    /**
     * Создание данных пользователя
     * */
    @PostMapping
    UserDto createUser(@RequestBody @Valid NewUserRequestDto newUserRequestDto) {
        log.debug("Получен запрос на добавление пользователя");
        return userAdminService.addUser(newUserRequestDto);
    }

    /**
     * Получение данных пользователей по списку ID, через параметр запроса
     * */
    @GetMapping
    List<UserDto> getUsers(@RequestParam List<Integer> ids,
                           @RequestParam(value = "from", defaultValue = "0") int from,
                           @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        log.debug("Получен запрос на получение данных пользователей по списку ID, через параметр запроса");
        return userAdminService.getUsers(ids, pageRequest);
    }

    /**
     * Удаление данных пользователя по ID
     * */
    @DeleteMapping("/{userId}")
    UserDto deleteUser(@PathVariable long userId) {
        log.debug("Получен запрос на удаление пользователя");
        return userAdminService.deleteUser(userId);
    }
}
