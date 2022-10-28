package ru.practicum.server.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

}
