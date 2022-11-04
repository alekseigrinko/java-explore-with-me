package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class HitDto {
    private Long id;

    /**
     * Название приложения, через который был получен запрос
     * */
    private String app;

    /**
     * URI запроса
     * */
    private String uri;

    /**
     * IP-адрес, с которого поступил запрос
     * */
    private String ip;

    /**
     * Время запроса
     * */
    private String timestamp;
}
