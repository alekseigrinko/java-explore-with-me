package ru.practicum.server.comment.contoroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentRequestDto;
import ru.practicum.server.comment.dto.NewCommentDto;
import ru.practicum.server.comment.service.pvt.CommentPrivateService;

import java.util.List;

/**
 * Контроллер пользователя для работы с данными комментариев
 */
@RestController
@Slf4j
@RequestMapping(path = "users/{userId}/comments")
public class PrivateCommentController {

    /**
     * @see ru.practicum.server.comment.service.pvt.CommentPrivateService - интерефейс методов пользователя по работе с комментариями
     */
    private final CommentPrivateService commentPrivateService;

    public PrivateCommentController(CommentPrivateService commentPrivateService) {
        this.commentPrivateService = commentPrivateService;
    }

    /**
     * Размещение данных нового комментария
     */
    @PostMapping("/{eventId}")
    CommentRequestDto postComment(@PathVariable long eventId,
                                  @PathVariable long userId,
                                  @RequestBody NewCommentDto newCommentDto) {
        log.debug("Направление запроса на размещение комментария");
        return commentPrivateService.postComment(newCommentDto, eventId, userId);
    }

    /**
     * Обновление комментария пользователя
     */
    @PatchMapping("/{commentId}")
    CommentRequestDto updateComment(@PathVariable long userId,
                                    @PathVariable long commentId,
                                    @RequestBody NewCommentDto newCommentDto) {
        log.debug("Направление запроса на обновление комментария");
        return commentPrivateService.updateComment(newCommentDto, commentId, userId);
    }

    /**
     * Получение данных комментария пользователя по ID
     */
    @GetMapping("/{commentId}")
    CommentRequestDto getCommentById(@PathVariable long userId,
                                     @PathVariable long commentId) {
        log.debug("Направление запроса на получение комментария пользователя по ID");
        return commentPrivateService.getCommentById(commentId, userId);
    }

    /**
     * Получение списка комментариев пользователя к событиям
     */
    @GetMapping
    List<CommentRequestDto> getAllUserComments(@PathVariable long userId,
                                               @RequestParam(value = "from", defaultValue = "0") int from,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        log.debug("Направление запроса на получение списка комментариев пользователя к событиям");
        return commentPrivateService.getAllUserComments(userId, pageRequest);
    }
}
