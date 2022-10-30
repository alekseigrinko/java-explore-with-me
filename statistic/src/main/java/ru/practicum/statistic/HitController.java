package ru.practicum.statistic;

import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.HitResponseDto;
import ru.practicum.statistic.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class HitController {

    private final HitService hitService;

    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping(path = "hit")
    HitDto postHit(@RequestBody HitDto hitDto) {
        return hitService.postHit(hitDto);
    }

    @GetMapping(path = "stats")
    public List<HitResponseDto> getHits(@RequestParam(value = "start") LocalDateTime start,
                                        @RequestParam(value = "end") LocalDateTime end,
                                        @RequestParam(value = "uris") List<String> uris,
                                        @RequestParam(value = "unique", defaultValue = "false") boolean unique) {

        return hitService.getHits(start, end, uris, unique);
    }

    @GetMapping(path = "views")
    public long getViews(@RequestParam(value = "uri") String uri) {
        return hitService.getViews(uri);
    }
}
