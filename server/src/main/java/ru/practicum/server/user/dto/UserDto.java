package ru.practicum.server.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

/**
 * DTO-класс для возврата данных пользователя
 * @see ru.practicum.server.user.model.User
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String email;
}
