package ru.practicum.statistic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название приложения, через который был получен запрос
     * Не может быть пустым
     * */
    @Column(name = "app", nullable = false)
    private String app;

    /**
     * URI запроса
     * Не может быть пустым
     * */
    @Column(name = "uri", nullable = false)
    private String uri;

    /**
     * IP-адрес, с которого поступил запрос
     * Не может быть пустым
     * */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * Время и дата запроса
     * */
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
