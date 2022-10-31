package ru.practicum.server.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.compilation.model.EventCompilation;

import javax.transaction.Transactional;
import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    @Transactional
    @Modifying
    @Query(" delete from EventCompilation e " +
            "where e.compilationId = ?1 and e.eventId = ?2 ")
    void deleteByCompilationIdAndEventId(long compilationId, long eventId);

    @Transactional
    @Modifying
    @Query(" delete from EventCompilation e " +
            "where e.compilationId = ?1 ")
    void deleteAllByCompilationId(long compilationId);

    @Query(" select e from EventCompilation e " +
            "where e.compilationId = ?1 ")
    List<EventCompilation> findAllByCompilationId(long compilationId);

}
