package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO-класс для получения данных статистики
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {

    /**
     * Название приложения, через которое делались запросы
     * */
    private String app;

    /**
     * URI - по которому были запросы
     * */
    private String uri;

    /**
     * Количество просмотров
     * */
    private long hits;
}
