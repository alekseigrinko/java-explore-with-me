package ru.practicum.server.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Параметр имени пользователя
     * Не может быть пустым
     * */
    @Column(name = "name", nullable = false)
    String name;

    /**
     * Параметр адреса электронной почты
     * Не может быть пустым
     * */
    @Column(name = "email", nullable = false)
    String email;
}
