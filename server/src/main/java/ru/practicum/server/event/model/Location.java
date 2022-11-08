package ru.practicum.server.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Класс модели "Location" для работы с моделью "Event" и ее DTO-классом
 * @see Event
 * @see ru.practicum.server.event.dto.EventFullDto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    /**
     * Параметр ширины координат локации проведения события
     * */
    float lat;

    /**
     * Параметр долготы координат локации проведения события
     * */
    float lon;
}
