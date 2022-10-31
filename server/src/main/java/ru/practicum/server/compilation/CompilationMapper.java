package ru.practicum.server.compilation;

import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                null,
                newCompilationDto.getTitle(),
                newCompilationDto.isPinned(),
                LocalDateTime.now()
        );
    }

    public static CompilationDto toCompilationResponseDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.isPinned(),
                events
        );
    }
}
