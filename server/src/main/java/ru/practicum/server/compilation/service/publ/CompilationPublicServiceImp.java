package ru.practicum.server.compilation.service.publ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.compilation.repository.CompilationRepository;
import ru.practicum.server.compilation.repository.EventCompilationRepository;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.EventCompilation;
import ru.practicum.server.event.client.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exeption.NotFoundError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;

/**
 * Реализация публичного интерфейса по работе с данными подборок событий
 * @see CompilationPublicService
 * */
@Service
@Slf4j
public class CompilationPublicServiceImp implements CompilationPublicService {

    /**
     * Репозиторий событий
     * @see EventRepository
     * */
    private final EventRepository eventRepository;

    /**
     * Репозиторий подборок событий
     * @see CompilationRepository
     * */
    private final CompilationRepository compilationRepository;

    /**
     * Репозиторий подборок с привязкой к ID событий
     * @see EventCompilationRepository
     * */
    private final EventCompilationRepository eventCompilationRepository;

    /**
     * Репозиторий категорий
     * @see CategoryRepository
     * */
    private final CategoryRepository categoryRepository;

    /**
     * Репозиторий пользователей
     * @see UserRepository
     * */
    private final UserRepository userRepository;

    /**
     * Репозиторий запросов на участие в событиях
     * @see RequestRepository
     * */
    private final RequestRepository requestRepository;

    /**
     * Клиент для взаимодействия с сервисом статистики
     * @see EventClient
     * */
    private final EventClient eventClient;

    public CompilationPublicServiceImp(EventRepository eventRepository, CompilationRepository compilationRepository,
                                       EventCompilationRepository eventCompilationRepository,
                                       CategoryRepository categoryRepository, UserRepository userRepository,
                                       RequestRepository requestRepository, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
        this.eventCompilationRepository = eventCompilationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @see CompilationPublicService#getCompilation(long)
     * */
    @Override
    public CompilationDto getCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
        List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByCompilationId(compilation.getId());
        List<EventShortDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation : eventCompilationList) {
            eventCompilationRepository.save(new EventCompilation(
                    null,
                    eventCompilation.getEventId(),
                    compilation.getId()));
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(event.getId())
            ));
        }
        log.debug("Предоставлены данные событий по подборке ID: " + compilationId);
        return toCompilationDto(compilation, events);
    }

    /**
     * @see CompilationPublicService#getAllCompilations(boolean, PageRequest)
     * */
    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByCompilationId(compilation.getId());
            List<EventShortDto> events = new ArrayList<>();
            for (EventCompilation eventCompilation : eventCompilationList) {
                eventCompilationRepository.save(new EventCompilation(
                        null,
                        eventCompilation.getEventId(),
                        compilation.getId()));
                Event event = eventRepository.findById(eventCompilation.getEventId()).get();
                events.add(toEventShortDto(
                        event,
                        userRepository.findById(event.getInitiatorId()).get(),
                        categoryRepository.findById(event.getCategoryId()).get(),
                        eventClient.getViews(event.getId()),
                        requestRepository.getEventParticipantLimit(event.getId()
                )));
            }
            compilationDtoList.add(toCompilationDto(compilation, events));
        }
        log.debug("Предоставлены подборки по запросу");
        return compilationDtoList;
    }

    /**
     * Метод проверки наличия подборки по ID в репозитории
     * @throws NotFoundError - при отсутствии подборки по ID
     * */
    public void checkCompilation(long compilationId) {
        if (!compilationRepository.existsById(compilationId)) {
            log.warn("Подборки ID: " + compilationId + ", не найдено!");
            throw new NotFoundError("Подборки ID: " + compilationId + ", не найдено!");
        }
    }
}
