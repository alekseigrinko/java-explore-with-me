package ru.practicum.server.compilation.service.publ;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.compilation.dto.CompilationDto;

import java.util.List;

/**
 * Публичный интерфейс по работе с данными подборок событий
 * */
public interface CompilationPublicService {

    /**
     * Метод получения данных подборок
     * @param pinned - параметр закрепления/ не закрепления подборки на главной странице
     * @return возвращает список DTO с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    List<CompilationDto> getAllCompilations(boolean pinned, PageRequest pageRequest);

    /**
     * Метод получения подборки по ID
     * @param compilationId - ID подборки
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    CompilationDto getCompilation(long compilationId);

}
