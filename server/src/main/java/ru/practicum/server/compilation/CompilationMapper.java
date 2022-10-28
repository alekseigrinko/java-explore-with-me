package ru.practicum.server.compilation;

import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.CompilationResponseDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.event.dto.EventResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(CompilationDto compilationDto) {
        return new Compilation(
                compilationDto.getId(),
                compilationDto.getTitle(),
                compilationDto.isPinned(),
                LocalDateTime.now()
        );
    }

    public static CompilationResponseDto toCompilationResponseDto(Compilation compilation, List<EventResponseDto> events) {
        return new CompilationResponseDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.isPinned(),
                events
        );
    }
}
