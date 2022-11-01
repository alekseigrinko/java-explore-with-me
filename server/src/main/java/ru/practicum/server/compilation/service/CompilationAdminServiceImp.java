package ru.practicum.server.compilation.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.compilation.CompilationRepository;
import ru.practicum.server.compilation.EventCompilationRepository;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.EventCompilation;
import ru.practicum.server.event.EventClient;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exeption.ApiError;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.compilation.CompilationMapper.toCompilation;
import static ru.practicum.server.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;


@Service
@Slf4j
public class CompilationAdminServiceImp implements CompilationAdminService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventClient eventClient;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


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

    @Override
    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
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

    @Override
    public CompilationDto unpinnedCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
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

    @Override
    public CompilationDto pinnedCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
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

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("События ID: " + eventId + ", не найдено!");
            throw new ApiError();
        }
    }

    public void checkCompilation(long compilationId) {
        if (!compilationRepository.existsById(compilationId)) {
            log.warn("Подборки ID: " + compilationId + ", не найдено!");
            throw new ApiError();
        }
    }
}
