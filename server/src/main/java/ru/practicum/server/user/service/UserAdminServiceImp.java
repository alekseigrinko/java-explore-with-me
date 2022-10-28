package ru.practicum.server.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.user.UserMapper;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserDto addUser(UserDto userDto) {
        return toUserDto(userRepository.save(toUser(userDto)));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        checkUser(userId);
        User userInMemory = userRepository.findById(userId).get();
        if (userDto.getName() != null) {
            userInMemory.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userInMemory.setEmail(userDto.getEmail());
        }
        log.debug("Данные пользователя обновлены: " + userInMemory);
        return toUserDto(userRepository.save(userInMemory));
    }

    @Override
    public List<UserDto> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDto> getUsers(List<Long> ids, PageRequest pageRequest) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (long id : ids) {
            checkUser(id);
            userDtoList.add(toUserDto(userRepository.findById(id).get()));
        }
        Page<UserDto> userDtoPage = new PageImpl<>(userDtoList, pageRequest, pageRequest.getPageSize());
        return userDtoPage;
    }

    @Override
    public UserDto deleteUser(long userId) {
        UserDto userDto = toUserDto(userRepository.findById(userId).get());
        userRepository.deleteById(userId);
        return userDto;
    }

    public void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }
}
