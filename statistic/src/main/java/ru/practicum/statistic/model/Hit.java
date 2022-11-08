package ru.practicum.statistic.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Этот класс модели "Hit" для сохранения в репозиторий
 * @see ru.practicum.statistic.HitRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Название приложения, через который был получен запрос
     * Не может быть пустым
     * */
    @Column(name = "app", nullable = false)
    String app;

    /**
     * URI запроса
     * Не может быть пустым
     * */
    @Column(name = "uri", nullable = false)
    String uri;

    /**
     * IP-адрес, с которого поступил запрос
     * Не может быть пустым
     * */
    @Column(name = "ip", nullable = false)
    String ip;

    /**
     * Время и дата запроса
     * */
    @Column(name = "timestamp")
    LocalDateTime timestamp;
}
