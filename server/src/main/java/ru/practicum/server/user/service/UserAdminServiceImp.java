package ru.practicum.server.user.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.dto.NewUserRequestDto;
import ru.practicum.server.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.user.UserMapper.toUser;
import static ru.practicum.server.user.UserMapper.toUserDto;

/**
 * реализация интерфейса администратора по работе с данными пользователя
 *
 * @see UserAdminService
 */
@Service
@Slf4j
public class UserAdminServiceImp implements UserAdminService {

    /**
     * репозиторий пользователей
     *
     * @see UserRepository
     */
    private final UserRepository userRepository;

    public UserAdminServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param newUserRequestDto - не должен быть null
     * @see UserAdminService#addUser(NewUserRequestDto)
     */
    @Override
    public UserDto addUser(@NonNull NewUserRequestDto newUserRequestDto) {
        log.debug("Зарегистрирован пользователь " + newUserRequestDto.getName());
        return toUserDto(userRepository.save(toUser(newUserRequestDto)));
    }

    /**
     * @param ids - не должен быть null
     * @see UserAdminService#getUsers(List, PageRequest)
     */
    @Override
    public List<UserDto> getUsers(@NonNull List<Integer> ids, PageRequest pageRequest) {
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

    /**
     * @throws NotFoundError - при отсутствии пользователя по ID
     * @see UserAdminService#deleteUser(long)
     */
    @Override
    public UserDto deleteUser(long userId) {
        UserDto userDto = toUserDto(userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new NotFoundError("Пользователь ID: " + userId + ", не найден!");
        }));
        userRepository.deleteUser(userId);
        log.debug("Удален пользователь ID: " + userId);
        return userDto;
    }
}
