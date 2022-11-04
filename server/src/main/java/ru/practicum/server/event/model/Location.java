package ru.practicum.server.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс модели "Location" для работы с моделью "Event" и ее DTO-классом
 * @see Event
 * @see ru.practicum.server.event.dto.EventFullDto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    /**
     * Параметр ширины координат локации проведения события
     * */
    private float lat;

    /**
     * Параметр долготы координат локации проведения события
     * */
    private float lon;
}
