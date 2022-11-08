package ru.practicum.statistic.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistic.HitRepository;
import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.statistic.HitMapper.toHit;
import static ru.practicum.statistic.HitMapper.toHitDto;

/**
 * Реализация интерфейса по работе с данными статистики
 * @see HitService
 * */
@Service
@Slf4j
public class HitServiceImp implements HitService {

    /**
     * Репозиторий данных статистики
     * @see HitRepository
     * */
    private final HitRepository hitRepository;

    public HitServiceImp(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }


    /**
     * @see HitService#postHit(HitDto)
     * @param hitDto - не должно равняться null
     * */
    @Override
    public HitDto postHit(@NonNull HitDto hitDto) {
        log.debug("Запись сохранена в лог: " + hitDto.getUri());
        return toHitDto(hitRepository.save(toHit(hitDto)));
    }

    /**
     * @see HitService#getHits(LocalDateTime, LocalDateTime, List, boolean) (HitDto)
     * @param uris - не должно равняться null
     * */
    @Override
    public List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, @NonNull List<String> uris, boolean unique) {
        List<ViewStatsDto> viewStatsDtoList = new ArrayList<>();
        if (unique) {
            for (String uri : uris) {
                viewStatsDtoList.add(new ViewStatsDto(
                        null,
                        uri,
                        hitRepository.getUniqueHits(start, end, uri)
                ));
            }
        } else {
            for (String uri : uris) {
                viewStatsDtoList.add(new ViewStatsDto(
                        null,
                        uri,
                        hitRepository.getHits(start, end, uri)
                ));
            }
        }
        log.debug("Предоставлены журнал данных по запросу");
        return viewStatsDtoList;
    }

    /**
     * @see HitService#getViews(String)
     * */
    @Override
    public long getViews(String uri) {
        return hitRepository.getViews(uri);
    }
}
