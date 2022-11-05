package ru.practicum.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    public static class CommentShortDto {
        private String description;
        private UserShortDto author;
    }
}
