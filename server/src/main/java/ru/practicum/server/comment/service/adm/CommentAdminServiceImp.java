package ru.practicum.server.comment.service.adm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.comment.CommentRepository;
import ru.practicum.server.comment.dto.CommentRequestDto;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import static ru.practicum.server.comment.CommentMapper.toCommentRequestDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;

/**
 * реализация интерфейса администратора по работе с данными комментариев пользователей
 * @see CommentAdminService
 */
@Service
@Slf4j
public class CommentAdminServiceImp implements CommentAdminService {

    /**
     * репозиторий событий
     *
     * @see EventRepository
     */
    private final EventRepository eventRepository;

    /**
     * репозиторий пользователей
     *
     * @see UserRepository
     */
    private final UserRepository userRepository;

    /**
     * репозиторий категорий
     *
     * @see CategoryRepository
     */
    private final CategoryRepository categoryRepository;

    /**
     * репозиторий комментариев пользователей
     *
     * @see CommentRepository
     */
    private final CommentRepository commentRepository;

    /**
     * Репозиторий запросов на участие в событиях
     *
     * @see RequestRepository
     */
    private final RequestRepository requestRepository;

    /**
     * клиент для взаимодействия с сервисом статистики
     *
     * @see EventClient
     */
    private final EventClient eventClient;


    public CommentAdminServiceImp(EventRepository eventRepository, UserRepository userRepository,
                                  CategoryRepository categoryRepository, CommentRepository commentRepository,
                                  RequestRepository requestRepository, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @see CommentAdminService#deleteComment(long)
     */
    @Override
    public CommentRequestDto deleteComment(long commentId) {
        checkComment(commentId);
        Comment comment = commentRepository.findById(commentId).get();
        commentRepository.deleteById(commentId);
        log.debug("Удален комментарий ID: " + commentId);
        return returnCommentRequestDto(comment);
    }

    private void checkComment(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            log.warn("Комментария ID: " + commentId + ", не найдено!");
            throw new NotFoundError("Комментария ID: " + commentId + ", не найдено!");
        }
    }

    /**
     * метод конвертации данных в DTO комментария
     *
     * @see CommentRequestDto
     * @see ru.practicum.server.comment.CommentMapper#toCommentRequestDto(Comment, EventShortDto, User)
     */
    private CommentRequestDto returnCommentRequestDto(Comment comment) {
        User user = userRepository.findById(comment.getAuthor()).get();
        Event event = eventRepository.findById(comment.getEvent()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        EventShortDto eventShortDto = toEventShortDto(
                event,
                user,
                category,
                eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId())
        );
        return toCommentRequestDto(comment, eventShortDto, user);
    }
}
