package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
