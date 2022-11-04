package ru.practicum.statistic;

import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Маппер для работы с моделями и DTO-классами пакета statistic
 * */
public class HitMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Метод конвертации данных статистики в DTO-класс
     * */
    public static HitDto toHitDto(Hit hit) {
        return new HitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp().format(formatter)
        );
    }

    /**
     * Метод конвертации данных статистики из DTO-класса в модель Hit
     * */
    public static Hit toHit(HitDto hitDto) {
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), formatter)
        );
    }
}
