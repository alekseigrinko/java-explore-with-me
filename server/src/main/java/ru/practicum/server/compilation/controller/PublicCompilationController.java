package ru.practicum.server.compilation.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.service.publ.CompilationPublicService;

import java.util.List;

/**
 * Публичный контроллер для работы с данными подборки событий
 * */
@RestController
@RequestMapping
public class PublicCompilationController {

    /**
     * @see CompilationPublicService- публичный интерефейс методов
     * */
    private final CompilationPublicService compilationPublicService;

    public PublicCompilationController(CompilationPublicService compilationPublicService) {
        this.compilationPublicService = compilationPublicService;
    }

    /**
     * Получение данных подборки по ID
     * */
    @GetMapping(path = "compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        return compilationPublicService.getCompilation(compId);
    }

    /**
     * Получение данных подборок с учетом параметра закрепления/ не закрепления подборки на главной странице
     * */
    @GetMapping(path = "compilations")
    List<CompilationDto> getAllCompilations(@RequestParam(value = "from", defaultValue = "0") int from,
                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                            @RequestParam(value = "pinned", defaultValue = "false") boolean pinned
    ) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("title").ascending());
        return compilationPublicService.getAllCompilations(pinned, pageRequest);
    }
}
