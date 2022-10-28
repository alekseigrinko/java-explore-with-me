package ru.practicum.server.compilation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.CompilationResponseDto;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface CompilationAdminService {

    CompilationResponseDto postCompilation(CompilationDto compilationDto);

    void putEventToCompilation(long compilationId, long eventId);

    void deleteEventFromCompilation(long compilationId, long eventId);

    void deleteCompilation(long compilationId);

    void unpinnedCompilation(long compilationId);

    void pinnedCompilation(long compilationId);
}
