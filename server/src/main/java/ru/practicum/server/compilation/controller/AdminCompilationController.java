package ru.practicum.server.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.service.adm.CompilationAdminService;

/**
 * Контроллер администратора для работы с данными подборок событий
 * */
@RestController
@Slf4j
@RequestMapping(path = "admin/compilations")
public class AdminCompilationController {

    /**
     * @see CompilationAdminService - интерефейс методов администратора
     * */
    private final CompilationAdminService compilationAdminService;

    public AdminCompilationController(CompilationAdminService compilationAdminService) {
        this.compilationAdminService = compilationAdminService;
    }

    /**
     * Создание данных новой подборки
     * */
    @PostMapping
    CompilationDto postCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationAdminService.postCompilation(newCompilationDto);
    }

    /**
     * Удаление подборки по ID
     * */
    @DeleteMapping("/{compId}")
    CompilationDto deleteCompilation(@PathVariable long compId) {
        return compilationAdminService.deleteCompilation(compId);
    }

    /**
     * Удаление события по ID из подборки
     * */
    @DeleteMapping("/{compId}/events/{eventId}")
    CompilationDto deleteEventFromCompilation(@PathVariable long compId,
                                              @PathVariable long eventId) {
        return compilationAdminService.deleteEventFromCompilation(compId, eventId);
    }

    /**
     * Добавление события в подборку
     * */
    @PatchMapping("/{compId}/events/{eventId}")
    CompilationDto putEventFromCompilation(@PathVariable long compId,
                                           @PathVariable long eventId) {
        return compilationAdminService.putEventToCompilation(compId, eventId);
    }

    /**
     * Открепить подборку от главной странице
     * */
    @DeleteMapping("/{compId}/pin")
    CompilationDto unpinnedCompilation(@PathVariable long compId) {
        return compilationAdminService.unpinnedCompilation(compId);
    }

    /**
     * Закрепить подборку на главной странице
     * */
    @PatchMapping("/{compId}/pin")
    CompilationDto pinnedCompilation(@PathVariable long compId) {
        return compilationAdminService.pinnedCompilation(compId);
    }
}
