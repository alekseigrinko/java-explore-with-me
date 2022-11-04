package ru.practicum.server.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс модели "Request" для сохранения в репозиторий по запрос на участие в событиях
 * @see ru.practicum.server.request.RequestRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Параметр ID пользователя, разместившего запрос на участие в событии
     * Не может быть пустым
     * */
    @Column(name = "requester_id", nullable = false)
    private long requester;

    /**
     * Параметр ID события
     * Не может быть пустым
     * */
    @Column(name = "event_id", nullable = false)
    private long event;

    /**
     * Дата создания запроса
     * Не может быть пустым
     * */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * Статус запроса на участие в событии
     * */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
