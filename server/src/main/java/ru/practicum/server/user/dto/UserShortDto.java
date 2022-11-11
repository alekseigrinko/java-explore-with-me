package ru.practicum.server.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {
    @NotNull
    Long id;
    @NotBlank
    String name;
}
