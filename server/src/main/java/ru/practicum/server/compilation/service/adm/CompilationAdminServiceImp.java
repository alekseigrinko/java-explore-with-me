package ru.practicum.server.compilation.service.adm;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.compilation.repository.CompilationRepository;
import ru.practicum.server.compilation.repository.EventCompilationRepository;
import ru.practicum.server.compilation.dto.NewCompilationDto;
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

import static ru.practicum.server.compilation.CompilationMapper.toCompilation;
import static ru.practicum.server.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;

/**
 * Реализация интерфейса администратора по работе с данными категорий
 * @see CompilationAdminService
 * */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationAdminServiceImp implements CompilationAdminService {

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

    public CompilationAdminServiceImp(EventRepository eventRepository, CompilationRepository compilationRepository,
                                      EventCompilationRepository eventCompilationRepository, CategoryRepository categoryRepository,
                                      UserRepository userRepository, RequestRepository requestRepository, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
        this.eventCompilationRepository = eventCompilationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    /**
     * @see CompilationAdminService#postCompilation(NewCompilationDto)
     * @param newCompilationDto - не должен быть null
     * */
    @Override
    public CompilationDto postCompilation(@NonNull NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(toCompilation(newCompilationDto));
        List<EventShortDto> events = new ArrayList<>();
        for (Integer id: newCompilationDto.getEvents()) {
            checkEvent(id);
            eventCompilationRepository.save(new EventCompilation(
                    null,
                    id,
                    compilation.getId()));
            Event event = eventRepository.findById(id.longValue()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(id)
            ));
        }
        log.debug("Сохранена подборка: " + compilation.getTitle());
        return toCompilationDto(compilation, events);
    }

    /**
     * @see CompilationAdminService#putEventToCompilation(long, long)
     * */
    @Override
    public CompilationDto putEventToCompilation(long compilationId, long eventId) {
        checkCompilation(compilationId);
        checkEvent(eventId);
        eventCompilationRepository.save(new EventCompilation(
                null,
                eventId,
                compilationId));
        List<EventShortDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation: eventCompilationRepository.findAllByCompilationId(compilationId)) {
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(eventCompilation.getEventId())
            ));
        }
        log.debug("Событие добавлено в подборку");
        return toCompilationDto(compilationRepository.findById(compilationId).get(), events);
    }

    /**
     * @see CompilationAdminService#deleteEventFromCompilation(long, long)
     * */
    @Override
    public CompilationDto deleteEventFromCompilation(long compilationId, long eventId) {
        checkCompilation(compilationId);
        checkEvent(eventId);
        eventCompilationRepository.deleteByCompilationIdAndEventId(compilationId, eventId);
        List<EventShortDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation: eventCompilationRepository.findAllByCompilationId(compilationId)) {
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(eventCompilation.getEventId())
            ));
        }
        log.debug("Событие удалено из подборки");
        return toCompilationDto(compilationRepository.findById(compilationId).get(), events);
    }

    /**
     * @see CompilationAdminService#deleteCompilation(long)
     * */
    @Override
    public CompilationDto deleteCompilation(long compilationId) {
        checkCompilation(compilationId);
        List<EventShortDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation: eventCompilationRepository.findAllByCompilationId(compilationId)) {
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(eventCompilation.getEventId())
            ));
        }
        CompilationDto compilationDto = toCompilationDto(compilationRepository.findById(compilationId).get(), events);
        eventCompilationRepository.deleteAllByCompilationId(compilationId);
        compilationRepository.deleteCompilation(compilationId);
        log.debug("Удалена подборка: " + compilationDto.getTitle());
        return compilationDto;
    }

    /**
     * @see CompilationAdminService#unpinnedCompilation(long)
     * */
    @Override
    public CompilationDto unpinnedCompilation(long compilationId) {
        Compilation compilation = returnCompilationWithCheck(compilationId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.debug("Откреплена от главной страницы подборка: " + compilation.getTitle());
        List<EventShortDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation: eventCompilationRepository.findAllByCompilationId(compilationId)) {
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(eventCompilation.getEventId())
            ));
        }
        return toCompilationDto(compilationRepository.findById(compilationId).get(), events);
    }

    /**
     * @see CompilationAdminService#pinnedCompilation(long)
     * */
    @Override
    public CompilationDto pinnedCompilation(long compilationId) {
        Compilation compilation = returnCompilationWithCheck(compilationId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.debug("Закреплена на главной страницы подборка: " + compilation.getTitle());
        List<EventShortDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation: eventCompilationRepository.findAllByCompilationId(compilationId)) {
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventShortDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    eventClient.getViews(event.getId()),
                    requestRepository.getEventParticipantLimit(eventCompilation.getEventId())
            ));
        }
        return toCompilationDto(compilationRepository.findById(compilationId).get(), events);
    }

    /**
     * Метод проверки наличия события по ID в репозитории
     * @throws NotFoundError - при отсутствии события по ID
     * */
    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("События ID: " + eventId + ", не найдено!");
            throw new NotFoundError("События ID: " + eventId + ", не найдено!");
        }
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
