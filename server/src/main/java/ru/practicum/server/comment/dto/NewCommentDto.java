package ru.practicum.server.comment.dto;

import lombok.*;
import ru.practicum.server.comment.model.Comment;

import javax.validation.constraints.NotBlank;

/**
 * DTO-класс нового комментария пользователя
 * @see Comment
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank
    private String description;
}
