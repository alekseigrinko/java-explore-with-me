package ru.practicum.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * DTO-класс для регистрации данных нового пользователя
 * @see ru.practicum.server.user.model.User
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
