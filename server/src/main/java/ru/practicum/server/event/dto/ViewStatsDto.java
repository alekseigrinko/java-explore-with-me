package ru.practicum.server.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO-класс для получения данных статистики
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStatsDto {

    /**
     * Название приложения, через которое делались запросы
     * */
    String app;

    /**
     * URI - по которому были запросы
     * */
    String uri;

    /**
     * Количество просмотров
     * */
    long hits;
}
