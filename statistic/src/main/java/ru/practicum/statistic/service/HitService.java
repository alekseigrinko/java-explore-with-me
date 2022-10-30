package ru.practicum.statistic.service;

import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto postHit(HitDto hitDto);

    List<HitResponseDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    long getViews(String uri);

}
