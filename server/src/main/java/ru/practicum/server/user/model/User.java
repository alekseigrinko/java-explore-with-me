package ru.practicum.server.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Этот класс модели "User" для сохранения в репозиторий
 * @see ru.practicum.server.user.UserRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Параметр имени пользователя
     * Не может быть пустым
     * */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Параметр адреса электронной почты
     * Не может быть пустым
     * */
    @Column(name = "email", nullable = false)
    private String email;
}
