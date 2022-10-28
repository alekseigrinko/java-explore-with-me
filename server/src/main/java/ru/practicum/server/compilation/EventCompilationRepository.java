package ru.practicum.server.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.compilation.model.EventCompilation;

import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    void deleteByCompilationIdAndEventId(long compilationId, long eventId);

    void deleteAllByCompilationId(long compilationId);

    List<EventCompilation> findAllByCompilationId(long compilationId);

}
