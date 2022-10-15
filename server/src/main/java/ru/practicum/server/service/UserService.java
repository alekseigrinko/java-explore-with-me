package ru.practicum.server.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    List<UserDto> getAllUsers(PageRequest pageRequest);

    UserDto getUser(long userId);

    UserDto deleteUser(long userId);
}
