package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс для предоставления данных подборок событий
 * В DTO содержатся класс категории, пользователя
 * @see ru.practicum.server.event.model.Event
 * @see CategoryDto
 * @see UserShortDto
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    long confirmedRequests;
    @NotBlank
    private String eventDate;
    private long id;
    @NotNull
    private UserShortDto initiator;
    @NotBlank
    private String title;
    long views;

}
