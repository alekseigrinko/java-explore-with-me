package ru.practicum.server.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.compilation.CompilationRepository;
import ru.practicum.server.compilation.EventCompilationRepository;
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

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.server.event.EventMapper.toEventShortDto;


@Service
@Slf4j
public class CompilationPublicServiceImp implements CompilationPublicService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
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
