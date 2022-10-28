package ru.practicum.server.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface UserAdminService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    List<UserDto> getAllUsers(PageRequest pageRequest);

    Page<UserDto> getUsers(List<Long> ids, PageRequest pageRequest);

    UserDto deleteUser(long userId);
}
