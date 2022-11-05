package ru.practicum.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.user.dto.UserShortDto;

/**
 * DTO-класс для предоставления информации о комментарии пользователя
 * @see Comment
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private Long id;
    private EventShortDto event;
    private UserShortDto author;
    private String description;
    private String created;
}
