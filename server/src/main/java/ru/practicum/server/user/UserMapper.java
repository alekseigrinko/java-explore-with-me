package ru.practicum.server.user;

import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return new User(
                null,
                newUserRequest.getName(),
                newUserRequest.getEmail()
        );
    }
}
