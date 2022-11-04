package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.event.model.Location;
import ru.practicum.server.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс для возврата полных данных события
 * В DTO содержатся классы локации, категории, пользователя
 * @see ru.practicum.server.event.model.Event
 * @see Location
 * @see CategoryDto
 * @see UserShortDto
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    long confirmedRequests;
    private String createdOn;
    private String description;
    @NotBlank
    private String eventDate;
    private long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    Location location;
    @NotNull
    private boolean paid;
    long participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    @NotBlank
    private String title;
    long views;
}
