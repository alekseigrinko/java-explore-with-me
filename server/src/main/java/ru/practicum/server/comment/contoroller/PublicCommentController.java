package ru.practicum.server.comment.contoroller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
 * */
@RestController
@RequestMapping(path = "events")
public class PublicCommentController {

    /**
     * @see CommentPublicService - интерефейс публичных методов
     * */
    private final CommentPublicService commentPublicService;

    /**
     * Клиент для взаимодействия с сервисом статистики
     * @see EventClient
     * */
    private final EventClient eventClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PublicCommentController(CommentPublicService commentPublicService, EventClient eventClient) {
        this.commentPublicService = commentPublicService;
        this.eventClient = eventClient;
    }

    /**
     * Получение данных комментариев события по его ID
     * Направление данных запроса на сервер статистики
     * @see EventClient
     * */
    @GetMapping("/{id}/comments")
    public CommentEventRequestDto getCommentByEvent(@PathVariable long id,
                                           @RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                           HttpServletRequest request) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        eventClient.postHit(new HitDto(null, "ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now().format(formatter)));
        return commentPublicService.getCommentByEvent(id, pageRequest);
    }
}
