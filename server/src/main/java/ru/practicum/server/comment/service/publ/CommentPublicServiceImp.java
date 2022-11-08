package ru.practicum.server.comment.service.publ;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.comment.CommentRepository;
import ru.practicum.server.comment.dto.CommentEventRequestDto;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.comment.CommentMapper.toCommentEventRequestDto;
import static ru.practicum.server.comment.CommentMapper.toCommentShortDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;

/**
 * реализация публичного интерфейса по работе с данными комментариев пользователей
 *
 * @see CommentPublicService
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentPublicServiceImp implements CommentPublicService {

    /**
     * репозиторий событий
     *
     * @see EventRepository
     */
    EventRepository eventRepository;

    /**
     * репозиторий пользователей
     *
     * @see UserRepository
     */
    UserRepository userRepository;

    /**
     * репозиторий категорий
     *
     * @see CategoryRepository
     */
    CategoryRepository categoryRepository;

    /**
     * репозиторий комментариев пользователей
     *
     * @see CommentRepository
     */
    CommentRepository commentRepository;

    /**
     * Репозиторий запросов на участие в событиях
     *
     * @see RequestRepository
     */
    RequestRepository requestRepository;

    /**
     * клиент для взаимодействия с сервисом статистики
     *
     * @see EventClient
     */
    EventClient eventClient;

    public CommentPublicServiceImp(EventRepository eventRepository, CategoryRepository categoryRepository,
                                   UserRepository userRepository, CommentRepository commentRepository, RequestRepository requestRepository,
                                   EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @see CommentPublicService#getCommentByEvent(long)
     */
    @Override
    public CommentEventRequestDto getCommentByEvent(long eventId) {
        checkEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        List<CommentEventRequestDto.CommentShortDto> comments = commentRepository.findAllByEvent(eventId)
                .stream()
                .map(this::returnCommentShortDto)
                .collect(Collectors.toList());
        log.debug("Предоставлены данные по комментариям события ID: " + eventId);
        return toCommentEventRequestDto(returnEventShortDto(event), comments);
    }

    /**
     * метод проверки наличия события по ID в репозитории
     *
     * @throws NotFoundError - при отсутствии события по ID
     */
    private void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("Событие ID: " + eventId + ", не найдено!");
            throw new NotFoundError("Событие ID: " + eventId + ", не найдено!");
        }
    }

    /**
     * метод конвертации данных в краткий DTO комментария
     *
     * @see ru.practicum.server.comment.dto.CommentEventRequestDto.CommentShortDto
     * @see ru.practicum.server.comment.CommentMapper#toCommentShortDto(Comment, User)
     */
    private CommentEventRequestDto.CommentShortDto returnCommentShortDto(Comment comment) {
        User user = userRepository.findById(comment.getAuthor()).get();
        return toCommentShortDto(comment, user);
    }

    /**
     * метод конвертации данных в краткий DTO события
     *
     * @see EventShortDto
     * @see ru.practicum.server.event.EventMapper#toEventShortDto(Event, User, Category, long, long)
     */
    private EventShortDto returnEventShortDto(Event event) {
        User user = userRepository.findById(event.getInitiatorId()).get();
        Category category = categoryRepository.findById(event.getCategoryId()).get();
        return toEventShortDto(
                event,
                user,
                category,
                eventClient.getViews(event.getId()),
                requestRepository.getEventParticipantLimit(event.getId())
        );
    }
}
