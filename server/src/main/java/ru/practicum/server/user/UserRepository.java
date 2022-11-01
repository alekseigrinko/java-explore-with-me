package ru.practicum.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.user.model.User;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query(" delete from User u " +
            "where u.id = ?1 ")
    void deleteUser(long userId);
}
