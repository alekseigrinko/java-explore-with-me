package ru.practicum.server.compilation.service;

import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;

public interface CompilationAdminService {

    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    void putEventToCompilation(long compilationId, long eventId);

    void deleteEventFromCompilation(long compilationId, long eventId);

    void deleteCompilation(long compilationId);

    void unpinnedCompilation(long compilationId);

    void pinnedCompilation(long compilationId);
}
