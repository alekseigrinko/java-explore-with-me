package ru.practicum.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.user.model.User;

import javax.transaction.Transactional;

/**
 * Интерфейс для работы с репозиторием пользователей
 * @see ru.practicum.server.user.model.User
 * */

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Метод удаления записи пользователя по ID
     * @param userId - ID пользователя
     * */
    @Transactional
    @Modifying
    @Query(" delete from User u " +
            "where u.id = ?1 ")
    void deleteUser(long userId);
}
