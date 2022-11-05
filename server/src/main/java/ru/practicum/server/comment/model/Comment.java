package ru.practicum.server.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Параметр ID события, по которому дается комментарий.
     * Не может быть пустым
     * */
    @Column(name = "event_id", nullable = false)
    private long event;

    /**
     * Параметр ID пользователя, разместившего комментарий.
     * Не может быть пустым
     * */
    @Column(name = "author_id", nullable = false)
    private long author;

    /**
     * Содержание комментария
     * */
    @Column(name = "description")
    private String description;

    /**
     * Дата создания комментария.
     * Не может быть пустым
     * */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;


}
