package ru.practicum.server.event.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Класс для реализации фильтра для получения списка событий по праметрам запроса
 * @see ru.practicum.server.event.controller.AdminEventController#getAllEvents(int, int, List, List, List, String, String)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventFilter {
    List<String> states;
    List<Integer> categories;
    List<Integer> users;
    String rangeStart;
    String rangeEnd;
}
