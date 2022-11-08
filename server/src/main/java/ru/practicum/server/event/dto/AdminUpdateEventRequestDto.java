package ru.practicum.server.event.dto;

import lombok.*;
import ru.practicum.server.event.model.Location;

/**
 * DTO-класс для обновления данных события администратором
 * В DTO содержится класс локации
 * @see ru.practicum.server.event.model.Event
 * @see Location
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventRequestDto {
    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    Location location;
    private boolean paid;
    long participantLimit;
    private boolean requestModeration;
    private String title;
}
