package ru.practicum.server.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exeption.ApiError;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.user.UserMapper.toUser;
import static ru.practicum.server.user.UserMapper.toUserDto;

@Service
@Slf4j
public class UserAdminServiceImp implements UserAdminService {

    private final UserRepository userRepository;

    public UserAdminServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        log.debug("Зарегистрирован пользователь " + newUserRequest.getName());
        return toUserDto(userRepository.save(toUser(newUserRequest)));
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, PageRequest pageRequest) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (Integer id : ids) {
            if (userRepository.existsById(id.longValue())) {
                userDtoList.add(toUserDto(userRepository.findById(id.longValue()).get()));
            }
        }
        Page<UserDto> users = new PageImpl<>(userDtoList, pageRequest, pageRequest.getPageSize());
        log.debug("Предоставлены данные выбранных пользователей: " + ids);
        return users.toList();
    }

    @Override
    public UserDto deleteUser(long userId) {
        checkUser(userId);
        UserDto userDto = toUserDto(userRepository.findById(userId).get());
        userRepository.deleteUser(userId);
        log.debug("Удален пользователь ID: " + userId);
        return userDto;
    }

    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ApiError();
        }
    }
}
