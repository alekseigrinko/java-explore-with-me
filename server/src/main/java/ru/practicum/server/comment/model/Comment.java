package ru.practicum.server.comment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс модели "Comment" для сохранения в репозиторий комментариев пользователей
 * @see ru.practicum.server.request.RequestRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Параметр ID события, по которому дается комментарий.
     * Не может быть пустым
     * */
    @Column(name = "event_id", nullable = false)
    long event;

    /**
     * Параметр ID пользователя, разместившего комментарий.
     * Не может быть пустым
     * */
    @Column(name = "author_id", nullable = false)
    long author;

    /**
     * Содержание комментария
     * */
    @Column(name = "description")
    String description;

    /**
     * Дата создания комментария.
     * Не может быть пустым
     * */
    @Column(name = "created", nullable = false)
    LocalDateTime created;
}
