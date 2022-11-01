package ru.practicum.server.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface UserAdminService {

    UserDto addUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Integer> ids, PageRequest pageRequest);

    UserDto deleteUser(long userId);
}
