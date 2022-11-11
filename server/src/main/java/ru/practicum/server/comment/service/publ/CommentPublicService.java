package ru.practicum.server.comment.service.publ;

import ru.practicum.server.comment.dto.CommentEventRequestDto;

/**
 * публичный интерфейс по работе с данными комментариев пользователей
 * */
public interface CommentPublicService {

    /**
     * метод получения списка комментариев по событию
     * @param eventId - ID события
     * @return DTO события с комментариями пользователей
     * @see CommentEventRequestDto
     * */
    CommentEventRequestDto getCommentByEvent(long eventId);
}
