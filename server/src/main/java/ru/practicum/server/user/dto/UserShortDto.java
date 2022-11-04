package ru.practicum.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс для возврата данных пользователя в DTO-класс события
 * @see ru.practicum.server.event.dto.EventFullDto
 * @see ru.practicum.server.event.dto.EventShortDto
 * @see ru.practicum.server.user.model.User
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
