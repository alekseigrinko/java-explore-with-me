package ru.practicum.statistic.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO-класс для получения данных в сервер статистики
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HitDto {
    Long id;
    String app;
    String uri;
    String ip;
    String timestamp;
}
