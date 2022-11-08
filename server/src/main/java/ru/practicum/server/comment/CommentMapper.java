package ru.practicum.server.comment;

import ru.practicum.server.comment.dto.CommentEventRequestDto;
import ru.practicum.server.comment.dto.CommentRequestDto;
import ru.practicum.server.comment.dto.NewCommentDto;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.user.dto.UserShortDto;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Маппер для работы с моделями и DTO-классами пакета comment
 */
public class CommentMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Метод конвертации данных нового комментария из DTO-класса в модель Comment
     * @see NewCommentDto
     */
    public static Comment toComment(NewCommentDto newCommentDto, long eventId, long authorId) {
        return new Comment(
                null,
                eventId,
                authorId,
                newCommentDto.getDescription(),
                LocalDateTime.now()
        );
    }

    /**
     * Метод конвертации данных комментария из репозитория в DTO-класс.
     * Используется список DTO пользователя и события
     * @see EventShortDto
     * @see User
     * @see UserShortDto
     * @see CommentRequestDto
     */
    public static CommentRequestDto toCommentRequestDto(Comment comment, EventShortDto eventShortDto, User user) {
        return new CommentRequestDto(
                comment.getId(),
                eventShortDto,
                new UserShortDto(
                        user.getId(),
                        user.getName()
                ),
                comment.getDescription(),
                comment.getCreated().format(formatter)
        );
    }

    public static CommentEventRequestDto toCommentEventRequestDto(EventShortDto eventShortDto,
                                                                  List<CommentEventRequestDto.CommentShortDto> comments) {
        return new CommentEventRequestDto(
                eventShortDto,
                comments
        );
    }

    public static CommentEventRequestDto.CommentShortDto toCommentShortDto(Comment comment, User user) {
        return new CommentEventRequestDto.CommentShortDto(
                comment.getDescription(),
                new UserShortDto(
                        user.getId(),
                        user.getName()
                )
        );
    }
}
