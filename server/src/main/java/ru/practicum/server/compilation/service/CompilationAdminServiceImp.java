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
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.request.RequestRepository;
import ru.practicum.server.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.compilation.CompilationMapper.toCompilation;
import static ru.practicum.server.compilation.CompilationMapper.toCompilationResponseDto;
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
        return toCompilationResponseDto(compilation, events);
    }

    @Override
    public void putEventToCompilation(long compilationId, long eventId) {
        checkCompilation(compilationId);
        checkEvent(eventId);
        eventCompilationRepository.save(new EventCompilation(
                null,
                eventId,
                compilationId));
        log.debug("Событие добавлено в подборку");
    }

    @Override
    public void deleteEventFromCompilation(long compilationId, long eventId) {
        checkCompilation(compilationId);
        checkEvent(eventId);
        eventCompilationRepository.deleteByCompilationIdAndEventId(compilationId, eventId);
        log.debug("Событие удалено из подборки");
    }

    @Override
    public void deleteCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
        eventCompilationRepository.deleteAllByCompilationId(compilationId);
        compilationRepository.deleteById(compilationId);
        log.debug("Удалена подборка: " + compilation.getTitle());
    }

    @Override
    public void unpinnedCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.debug("Откреплена от главной страницы подборка: " + compilation.getTitle());
    }

    @Override
    public void pinnedCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.debug("Закреплена на главной страницы подборка: " + compilation.getTitle());
    }

    public void checkEvent(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            log.warn("События ID: " + eventId + ", не найдено!");
            throw new ObjectNotFoundException("События ID: " + eventId + ", не найдено!");
        }
    }

    public void checkCompilation(long compilationId) {
        if (!compilationRepository.existsById(compilationId)) {
            log.warn("Подборки ID: " + compilationId + ", не найдено!");
            throw new ObjectNotFoundException("Подборки ID: " + compilationId + ", не найдено!");
        }
    }
}
