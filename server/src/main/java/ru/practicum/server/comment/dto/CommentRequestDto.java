package ru.practicum.server.comment.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDto {
    Long id;
    EventShortDto event;
    UserShortDto author;
    String description;
    String created;
}
