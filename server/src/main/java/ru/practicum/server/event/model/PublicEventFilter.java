package ru.practicum.server.event.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Класс для реализации фильтра для получения списка событий по праметрам запроса
 * @see ru.practicum.server.event.controller.PublicEventController#getAllEvents(int, int, String, List, boolean, String, String, boolean, SortEvent, HttpServletRequest)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicEventFilter {
    String text;
    List<Integer> categories;
    boolean paid;
    String rangeStart;
    String rangeEnd;
}
