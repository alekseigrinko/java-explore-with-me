package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
