package ru.practicum.server.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
