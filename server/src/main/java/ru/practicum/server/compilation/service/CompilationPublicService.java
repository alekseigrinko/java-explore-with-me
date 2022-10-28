package ru.practicum.server.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.compilation.dto.CompilationResponseDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationResponseDto> getAllCompilations(PageRequest pageRequest);

    CompilationResponseDto getCompilation(long compilationId);

}
