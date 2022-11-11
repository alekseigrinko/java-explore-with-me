package ru.practicum.server.compilation.service.adm;

import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;

/**
 * Интерфейс администратора по работе с данными подборки событий
 * */
public interface CompilationAdminService {

    /**
     * Метод сохранения данных подборки
     * @param newCompilationDto - DTO c данными новой подборки
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see NewCompilationDto
     * @see CompilationDto
     * */
    CompilationDto postCompilation(NewCompilationDto newCompilationDto);

    /**
     * Метод добавления события в подборку
     * @param compilationId - ID подборки
     * @param eventId - ID события
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    CompilationDto putEventToCompilation(long compilationId, long eventId);

    /**
     * Метод удаления события из подборки
     * @param compilationId - ID подборки
     * @param eventId - ID события
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    CompilationDto deleteEventFromCompilation(long compilationId, long eventId);

    /**
     * Метод удаления подборки
     * @param compilationId - ID подборки
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    CompilationDto deleteCompilation(long compilationId);

    /**
     * Метод открепления подборки от главной страницы
     * @param compilationId - ID подборки
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    CompilationDto unpinnedCompilation(long compilationId);

    /**
     * Метод закрепления подборки на главной странице
     * @param compilationId - ID подборки
     * @return возвращает DTO-класс с данными подборки и списка DTO-событий
     * @see CompilationDto
     * */
    CompilationDto pinnedCompilation(long compilationId);
}
