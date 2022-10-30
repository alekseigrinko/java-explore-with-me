package ru.practicum.statistic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistic.HitRepository;
import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.HitResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.statistic.HitMapper.toHit;
import static ru.practicum.statistic.HitMapper.toHitDto;

@Service
@Slf4j
public class HitServiceImp implements HitService {

    private final HitRepository hitRepository;

    public HitServiceImp(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }


    @Override
    public HitDto postHit(HitDto hitDto) {
        return toHitDto(hitRepository.save(toHit(hitDto)));
    }

    @Override
    public List<HitResponseDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<HitResponseDto> hitResponseDtoList = new ArrayList<>();
        if (unique) {
            for (String uri : uris) {
                hitResponseDtoList.add(new HitResponseDto(
                        null,
                        uri,
                        hitRepository.getUniqueHits(start, end, uri)
                ));
            }
        } else {
            for (String uri : uris) {
                hitResponseDtoList.add(new HitResponseDto(
                        null,
                        uri,
                        hitRepository.getHits(start, end, uri)
                ));
            }
        }
        return hitResponseDtoList;
    }

    @Override
    public long getViews(String uri) {
        return hitRepository.getViews(uri);
    }
}
