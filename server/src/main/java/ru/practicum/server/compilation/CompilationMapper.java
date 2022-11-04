package ru.practicum.server.compilation;

import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Маппер для работы с моделями и DTO-классами пакета compilation
 * */
public class CompilationMapper {

    /**
     * Метод конвертации данных подборки из DTO-класса в модель Compilation
     * */
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                null,
                newCompilationDto.getTitle(),
                newCompilationDto.isPinned(),
                LocalDateTime.now()
        );
    }

    /**
     * Метод конвертации данных категории из репозитория в DTO-класс.
     * Используется список DTO событий
     * @see EventShortDto
     * */
    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.isPinned(),
                events
        );
    }
}
