package ru.practicum.server.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.comment.model.Comment;

/**
 * Интерфейс для работы с репозиторием комментариев пользователей
 *
 * @see Comment
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Метод получения всех комментариев пользователя
     * @param authorId - ID пользователя
     * @return возвращает страницу комментариев пользователя
     * @see Comment
     * */
    Page<Comment> findAllByAuthor(long authorId, PageRequest pageRequest);

    /**
     * Метод получения всех комментариев события
     * @param eventId - ID события
     * @return возвращает страницу комментариев на заданное событие
     * @see Comment
     * */
    Page<Comment> findAllByEvent(long eventId, PageRequest pageRequest);
}
