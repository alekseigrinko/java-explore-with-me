package ru.practicum.server.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.compilation.model.Compilation;

import javax.transaction.Transactional;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(boolean pinned, PageRequest pageRequest);

    @Transactional
    @Modifying
    @Query(" delete from Compilation c " +
            "where c.id = ?1 ")
    void deleteCompilation(long compilationId);
}
