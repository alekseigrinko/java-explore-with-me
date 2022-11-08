package ru.practicum.server.comment.contoroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentRequestDto;
import ru.practicum.server.comment.service.adm.CommentAdminService;

/**
 * Контроллер администратора для работы с данными комментариев пользователей
 * */
@RestController
@Slf4j
@RequestMapping(path = "admin/comments")
public class AdminCommentController {

    /**
     * @see ru.practicum.server.comment.service.adm.CommentAdminService - интерефейс методов администратора
     * */
    private final CommentAdminService commentAdminService;

    public AdminCommentController(CommentAdminService commentAdminService) {
        this.commentAdminService = commentAdminService;
    }

    /**
     * Удаление комментария
     * */
    @DeleteMapping("/{commentId}")
    public CommentRequestDto deleteComment(@PathVariable long commentId) {
        log.debug("Направление запроса на удаление комментария");
        return commentAdminService.deleteComment(commentId);
    }
}
