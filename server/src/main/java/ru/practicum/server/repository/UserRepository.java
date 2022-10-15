package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
