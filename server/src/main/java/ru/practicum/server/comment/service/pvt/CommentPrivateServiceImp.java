package ru.practicum.server.comment.service.pvt;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.comment.CommentRepository;
import ru.practicum.server.comment.dto.CommentRequestDto;
import ru.practicum.server.comment.dto.NewCommentDto;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.State;
import ru.practicum.server.exeption.BadRequestError;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.request.model.Status;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.comment.CommentMapper.toComment;
import static ru.practicum.server.comment.CommentMapper.toCommentRequestDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;

/**
 * реализация публичного интерфейса по работе с данными комментариев пользвоателей
 * @see CommentPrivateService
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentPrivateServiceImp implements CommentPrivateService {

    /**
     * репозиторий событий
     * @see EventRepository
     */
    EventRepository eventRepository;

    /**
     * репозиторий пользователей
     * @see UserRepository
     */
    UserRepository userRepository;

    /**
     * репозиторий категорий
     * @see CategoryRepository
     */
    CategoryRepository categoryRepository;

    /**
     * репозиторий комментариев пользователей
     * @see CommentRepository
     */
    CommentRepository commentRepository;

    /**
     * Репозиторий запросов на участие в событиях
     * @see RequestRepository
     */
    RequestRepository requestRepository;

    /**
     * клиент для взаимодействия с сервисом статистики
     * @see EventClient
     */
    EventClient eventClient;

    public CommentPrivateServiceImp(EventRepository eventRepository, UserRepository userRepository,
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
     * @param newCommentDto - не должно быть равно null
     * @see CommentPrivateService#postComment(NewCommentDto, long, long)
     * @see CommentPrivateServiceImp#checkBeforePostComment(long, long) - проверки до сохранения комментария
     */
    @Override
    public CommentRequestDto postComment(@NonNull NewCommentDto newCommentDto, long eventId, long authorId) {
        checkBeforePostComment(eventId, authorId);
        Comment comment = commentRepository.save(toComment(newCommentDto, eventId, authorId));
        log.debug("Добавлен комментарий : " + comment);
        return returnCommentRequestDto(comment);
    }

    /**
     * @param newCommentDto - не должно быть равно null
     * @see CommentPrivateService#updateComment(NewCommentDto, long, long)
     */
    @Override
    public CommentRequestDto updateComment(@NonNull NewCommentDto newCommentDto, long commentId, long authorId) {
        checkUser(authorId);
        Comment oldComment = returnCommentWithCheck(commentId);
        oldComment.setDescription(newCommentDto.getDescription());
        Comment comment = commentRepository.save(oldComment);
        log.debug("Обновлен комментарий ID: " + commentId);
        return returnCommentRequestDto(comment);
    }

    /**
     * @throws BadRequestError - не соответствие пользователя запроса с автором комментария
     * @see CommentPrivateService#getCommentById(long, long)
     */
    @Override
    public CommentRequestDto getCommentById(long commentId, long authorId) {
        Comment comment = returnCommentWithCheck(commentId);
        if (comment.getAuthor() != authorId) {
            log.warn("Пользователь не является автором комментария");
            throw new BadRequestError("Пользователь не является автором комментария");
        }
        log.debug("Предоставлены данные комментария ID: " + commentId);
        return returnCommentRequestDto(comment);
    }

    /**
     * @see CommentPrivateService#getAllUserComments(long, PageRequest)
     */
    @Override
    public List<CommentRequestDto> getAllUserComments(long authorId, PageRequest pageRequest) {
        checkUser(authorId);
        log.debug("Предоставлены все комментарии пользователя ID: " + authorId);
        return commentRepository.findAllByAuthor(authorId, pageRequest).stream()
                .map(this::returnCommentRequestDto)
                .collect(Collectors.toList());
    }

    /**
     * метод проверки наличия события по ID в репозитории
     * @throws NotFoundError - при отсутствии события по ID
     */
    private void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new NotFoundError("Событие ID: " + eventId + ", не найдено!");
        }
    }

    /**
     * метод проверки наличия пользователя по ID в репозитории
     * @throws NotFoundError - при отсутствии пользователя по ID
     */
    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new NotFoundError("Пользователь ID: " + userId + ", не найден!");
        }
    }

    /**
     * метод проверки наличия комментария по ID в репозитории и его получения
     * @return комментарий из репозитория по ID
     * @throws NotFoundError - при отсутствии комментария по ID
     */
    private Comment returnCommentWithCheck(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.warn("Комментария ID: " + commentId + ", не найдено!");
            throw new NotFoundError("Комментария ID: " + commentId + ", не найдено!");
        });
    }

    /**
     * метод проверки комментария до его размещения
     * @throws NotFoundError - при отсутствии пользователя по ID
     * @throws BadRequestError - событие не опубликовано, запрос автора на участие не подтвержден, автор является инициатором события
     */
    private void checkBeforePostComment(long eventId, long authorId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Пользователь ID: " + authorId + ", не найден!");
            throw new NotFoundError("Пользователь ID: " + authorId + ", не найден!");
        });
        if (event.getState() != State.PUBLISHED) {
            log.warn("Событие еще не опубликовано");
            throw new BadRequestError("Событие еще не опубликовано");
        }
        if (event.getInitiatorId() == authorId) {
            log.warn("Комментарий не может оставить инициатор события");
            throw new BadRequestError("Комментарий не может оставить инициатор события");
        }
        if (requestRepository.findByEventAndRequester(eventId, authorId).getStatus() != Status.CONFIRMED) {
            log.warn("Запрос автора комментария на участие в событии на подтвержден");
            throw new BadRequestError("Запрос автора комментария на участие в событии на подтвержден");
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
