package ru.practicum.statistic.dto;

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
    String app;
    String uri;
    long hits;
}
