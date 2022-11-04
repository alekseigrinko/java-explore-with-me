package ru.practicum.server.user;

import ru.practicum.server.user.dto.NewUserRequestDto;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

/**
 * Маппер для работы с моделями и DTO-классами пакета user
 * */
public class UserMapper {

    /**
     * Метод конвертации данных пользователя из репозитория в DTO-класс
     * */
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    /**
     * Метод конвертации данных пользователя из DTO-класса в модель User
     * */
    public static User toUser(NewUserRequestDto newUserRequestDto) {
        return new User(
                null,
                newUserRequestDto.getName(),
                newUserRequestDto.getEmail()
        );
    }
}
