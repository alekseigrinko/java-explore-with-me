package ru.practicum.statistic;


import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.HitResponseDto;
import ru.practicum.statistic.model.Hit;

public class HitMapper {
    public static HitDto toHitDto(Hit hit) {
        return new HitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public static Hit toHit(HitDto hitDto) {
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()
        );
    }
}
