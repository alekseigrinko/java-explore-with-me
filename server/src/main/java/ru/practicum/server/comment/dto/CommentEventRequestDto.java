package ru.practicum.server.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.user.dto.UserShortDto;

import java.util.List;

/**
 * DTO-класс для предоставления информации о комментариях пользователей по событию
 * @see Comment
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEventRequestDto {

    private EventShortDto event;
    private List<CommentShortDto> comments;

    /**
     * DTO-класс для предоставления о комментарии к событию
     * @see Comment
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CommentShortDto {
        String description;
        UserShortDto author;
    }
}
