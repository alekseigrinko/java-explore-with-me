package ru.practicum.server.compilation.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.compilation.model.Compilation;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Интерфейс для работы с репозиторием подборок событий
 * @see Compilation
 * */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    /**
     * Метод получения списка подборок
     * @param pinned - параметр закрепления/ не закрепления подборки событий на главной странице
     * @return список подборок
     * */
    List<Compilation> findAllByPinned(boolean pinned, PageRequest pageRequest);

    /**
     * Метод удаления подборки по ID
     * */
    @Transactional
    @Modifying
    @Query(" delete from Compilation c " +
            "where c.id = ?1 ")
    void deleteCompilation(long compilationId);
}
