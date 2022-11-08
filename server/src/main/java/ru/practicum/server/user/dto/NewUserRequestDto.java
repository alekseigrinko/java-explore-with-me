package ru.practicum.server.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

/**
 * DTO-класс для регистрации данных нового пользователя
 * @see ru.practicum.server.user.model.User
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequestDto {
    @NotBlank
    String name;
    @NotBlank
    String email;
}
