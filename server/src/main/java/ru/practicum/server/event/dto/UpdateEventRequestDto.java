package ru.practicum.server.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * DTO-класс для обновления данных события пользователем
 * @see ru.practicum.server.event.model.Event
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequestDto {
    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    @NotNull
    long eventId;
    private boolean paid;
    long participantLimit;
    private String title;
}
