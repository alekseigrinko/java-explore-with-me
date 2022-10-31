package ru.practicum.server.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationDto> getAllCompilations(boolean pinned, PageRequest pageRequest);

    CompilationDto getCompilation(long compilationId);

}
