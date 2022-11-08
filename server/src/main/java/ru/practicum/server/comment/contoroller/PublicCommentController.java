package ru.practicum.server.comment.contoroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentEventRequestDto;
import ru.practicum.server.comment.service.publ.CommentPublicService;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.dto.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Публичный контроллер для работы с данными комментариев пользователей
 */
@RestController
@Slf4j
@RequestMapping(path = "events")
public class PublicCommentController {

    /**
     * @see CommentPublicService - интерефейс публичных методов
     */
    private final CommentPublicService commentPublicService;

    /**
     * Клиент для взаимодействия с сервисом статистики
     *
     * @see EventClient
     */
    private final EventClient eventClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PublicCommentController(CommentPublicService commentPublicService, EventClient eventClient) {
        this.commentPublicService = commentPublicService;
        this.eventClient = eventClient;
    }

    /**
     * Получение данных комментариев события по его ID
     * Направление данных запроса на сервер статистики
     *
     * @see EventClient
     */
    @GetMapping("/{id}/comments")
    public CommentEventRequestDto getCommentByEvent(@PathVariable long id,
                                                    HttpServletRequest request) {
        eventClient.postHit(new HitDto(null, "ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter)));
        log.debug("Направление запроса на получение данных комментариев события по его ID");
        return commentPublicService.getCommentByEvent(id);
    }
}
