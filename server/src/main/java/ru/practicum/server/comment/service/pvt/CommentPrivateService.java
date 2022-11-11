package ru.practicum.server.comment.service.pvt;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.comment.dto.CommentRequestDto;
import ru.practicum.server.comment.dto.NewCommentDto;

import java.util.List;

/**
 * интерфейс пользователя по работе с данными комментариев
 * */
public interface CommentPrivateService {

    /**
     * метод сохранения данных комментария пользователя
     * @param newCommentDto - DTO нового комментария
     * @param eventId - ID события, на которое дается комментарий
     * @param authorId - ID автора комментария
     * @return DTO с полными данными комментариями после его сохранения
     * @see NewCommentDto`
     * @see CommentRequestDto
     * */
    CommentRequestDto postComment(NewCommentDto newCommentDto, long eventId, long authorId);

    /**
     * метод обновления данных комментария пользователя
     * @param newCommentDto - DTO с обновленными данными комментария
     * @param commentId - ID комментария
     * @param authorId - ID автора комментария
     * @return DTO с обновленными данными комментария
     * @see NewCommentDto`
     * @see CommentRequestDto
     * */
    CommentRequestDto updateComment(NewCommentDto newCommentDto, long commentId, long authorId);

    /**
     * метод получения комментария пользователя по ID
     * @param commentId - ID комментария
     * @param authorId - ID автора комментария
     * @return DTO с данными комментария
     * @see CommentRequestDto
     * */
    CommentRequestDto getCommentById(long commentId, long authorId);

    /**
     * метод получения всех комментариев конкретного пользователя
     * @param authorId - ID автора комментария
     * @return список DTO с данными комментариев
     * @see CommentRequestDto
     * */
    List<CommentRequestDto> getAllUserComments(long authorId, PageRequest pageRequest);
}
