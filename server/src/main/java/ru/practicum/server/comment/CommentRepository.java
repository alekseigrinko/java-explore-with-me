package ru.practicum.server.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.comment.model.Comment;

import java.util.List;

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
     * @return возвращает список комментариев на заданное событие
     * @see Comment
     * */
    @Query("select c from Comment c " +
            " where c.event = ?1" +
            " order by c.created desc ")
    List<Comment> findAllByEvent(long eventId);
}
