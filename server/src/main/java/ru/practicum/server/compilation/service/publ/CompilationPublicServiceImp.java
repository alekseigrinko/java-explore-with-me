package ru.practicum.server.compilation.service.publ;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationPublicServiceImp implements CompilationPublicService {

    /**
     * Репозиторий событий
     * @see EventRepository
     * */
    EventRepository eventRepository;

    /**
     * Репозиторий подборок событий
     * @see CompilationRepository
     * */
    CompilationRepository compilationRepository;

    /**
     * Репозиторий подборок с привязкой к ID событий
     * @see EventCompilationRepository
     * */
    EventCompilationRepository eventCompilationRepository;

    /**
     * Репозиторий категорий
     * @see CategoryRepository
     * */
    CategoryRepository categoryRepository;

    /**
     * Репозиторий пользователей
     * @see UserRepository
     * */
    UserRepository userRepository;

    /**
     * Репозиторий запросов на участие в событиях
     * @see RequestRepository
     * */
    RequestRepository requestRepository;

    /**
     * Клиент для взаимодействия с сервисом статистики
     * @see EventClient
     * */
    EventClient eventClient;

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
        Compilation compilation = returnCompilationWithCheck(compilationId);
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

    /**
     * Метод проверки наличия подборки по ID в репозитории и его получения
     * @return возвращает подборку по ID
     * @throws NotFoundError - при отсутствии подборки по ID
     * */
    public Compilation returnCompilationWithCheck(long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() -> {
            log.warn("Подборки ID: " + compilationId + ", не найдено!");
            throw new NotFoundError("Подборки ID: " + compilationId + ", не найдено!");
        });
    }
}
