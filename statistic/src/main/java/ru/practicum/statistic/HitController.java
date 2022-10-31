package ru.practicum.statistic;

import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.ViewStats;
import ru.practicum.statistic.service.HitService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
public class HitController {

    private final HitService hitService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping(path = "hit")
    HitDto postHit(@RequestBody HitDto hitDto) {
        return hitService.postHit(hitDto);
    }

    @GetMapping(path = "stats")
    public List<ViewStats> getHits(@RequestParam(value = "start") String start,
                                   @RequestParam(value = "end") String end,
                                   @RequestParam(value = "uris") List<String> uris,
                                   @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        return hitService.getHits(startTime, endTime, uris, unique);
    }

    @GetMapping(path = "views")
    public long getViews(@RequestParam(value = "uri") String uri) {
        return hitService.getViews(uri);
    }
}
