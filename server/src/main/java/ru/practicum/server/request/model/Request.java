package ru.practicum.server.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс модели "Request" для сохранения в репозиторий запроса на участие в событиях
 * @see ru.practicum.server.request.RequestRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Параметр ID пользователя, разместившего запрос на участие в событии
     * Не может быть пустым
     * */
    @Column(name = "requester_id", nullable = false)
    long requester;

    /**
     * Параметр ID события
     * Не может быть пустым
     * */
    @Column(name = "event_id", nullable = false)
    long event;

    /**
     * Дата создания запроса
     * Не может быть пустым
     * */
    @Column(name = "created", nullable = false)
    LocalDateTime created;

    /**
     * Статус запроса на участие в событии
     * */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status;
}
