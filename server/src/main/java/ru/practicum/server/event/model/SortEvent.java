package ru.practicum.server.event.model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Список для сортировки событий, используемый в методе
 * @see ru.practicum.server.event.controller.PublicEventController#getAllEvents(int, int, String, List, boolean, String, String, boolean, SortEvent, HttpServletRequest)
 */
public enum SortEvent {
    /**
     * Сортировка по дате проведения события
     * */
    EVENT_DATE,

    /**
     * Сортировка по просмотрам события
     * */
    VIEWS
}
