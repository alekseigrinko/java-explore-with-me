package ru.practicum.server.compilation.service;

import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;

public interface CompilationAdminService {

    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    CompilationDto putEventToCompilation(long compilationId, long eventId);

    CompilationDto deleteEventFromCompilation(long compilationId, long eventId);

    CompilationDto deleteCompilation(long compilationId);

    CompilationDto unpinnedCompilation(long compilationId);

    CompilationDto pinnedCompilation(long compilationId);
}
