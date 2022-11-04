package ru.practicum.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO-класс для получения данных в сервер статистики
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
