package ru.practicum.server.comment.service.adm;

import ru.practicum.server.comment.dto.CommentRequestDto;

/**
 * интерфейс администратора по работе с комментариями пользователей
 * */
public interface CommentAdminService {

    /**
     * метод удаления комментария
     * @param commentId - ID комментария
     * @return возвращает DTO с данными удаленного комментария
     * @see CommentRequestDto
     * */
    CommentRequestDto deleteComment(long commentId);
}
