package ru.practicum.server.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.compilation.model.EventCompilation;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Интерфейс для работы с репозиторием привязки подборки событий и событий
 * @see EventCompilation
 * */
public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    /**
     * Метод удаления события из подборки
     * @param compilationId - ID события
     * @param eventId - ID события
     * */
    @Transactional
    @Modifying
    @Query(" delete from EventCompilation e " +
            "where e.compilationId = ?1 and e.eventId = ?2 ")
    void deleteByCompilationIdAndEventId(long compilationId, long eventId);

    /**
     * Метод удаления всех событий из подборки
     * @param compilationId - ID события
     * */
    @Transactional
    @Modifying
    @Query(" delete from EventCompilation e " +
            "where e.compilationId = ?1 ")
    void deleteAllByCompilationId(long compilationId);

    /**
     * Метод получения всех событий из подборки
     * @param compilationId - ID события
     * @return список событий с привязкой к подборке
     * @see EventCompilation
     * */
    @Query(" select e from EventCompilation e " +
            "where e.compilationId = ?1 ")
    List<EventCompilation> findAllByCompilationId(long compilationId);

}
