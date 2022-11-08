package ru.practicum.server.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.event.model.SortEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * DTO-класс для направления данных в сервер статистики, используется в публичном контроллере событий
 * @see ru.practicum.server.event.controller.PublicEventController#getAllEvents(int, int, String, List, boolean, String, String, boolean, SortEvent, HttpServletRequest)
 * @see ru.practicum.server.event.controller.PublicEventController#getEvent(long, HttpServletRequest)
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitDto {
    Long id;

    /**
     * Название приложения, через который был получен запрос
     * */
    String app;

    /**
     * URI запроса
     * */
    String uri;

    /**
     * IP-адрес, с которого поступил запрос
     * */
    String ip;

    /**
     * Время запроса
     * */
    String timestamp;
}
