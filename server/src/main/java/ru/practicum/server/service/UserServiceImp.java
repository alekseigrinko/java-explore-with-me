package ru.practicum.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.service.UserService;
import ru.practicum.server.user.UserMapper;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
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
        return UserMapper.toUserDto(userRepository.save(userInMemory));
    }

    @Override
    public List<UserDto> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long userId) {
        checkUser(userId);
        return UserMapper.toUserDto(userRepository.findById(userId).get());
    }

    @Override
    public UserDto deleteUser(long userId) {
        UserDto userDto = UserMapper.toUserDto(userRepository.findById(userId).get());
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
