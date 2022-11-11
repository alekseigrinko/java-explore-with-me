package ru.practicum.server.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.user.dto.NewUserRequestDto;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;

/**
 * интерфейс администратора по работе с данными пользователя
 * */
public interface UserAdminService {

    /**
     * метод сохранения данных пользователя
     * @param newUserRequestDto - DTO c данными нового пользователя
     * @return возвращает DTO-класс с данными пользователя и его присвоенного ID
     * @see NewUserRequestDto
     * @see UserDto
     * */
    UserDto addUser(NewUserRequestDto newUserRequestDto);

    /**
     * получения данных пользователей по списку ID
     * @param ids - список ID пользователей
     * @return список DTO по переданному запросу
     * @see UserDto
     * */
    List<UserDto> getUsers(List<Integer> ids, PageRequest pageRequest);

    /**
     * получения данных пользователей по списку ID
     * @param userId - ID пользователя
     * */
    UserDto deleteUser(long userId);
}
