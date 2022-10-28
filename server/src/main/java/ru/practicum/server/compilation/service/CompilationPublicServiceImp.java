package ru.practicum.server.compilation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.compilation.CompilationRepository;
import ru.practicum.server.compilation.EventCompilationRepository;
import ru.practicum.server.compilation.dto.CompilationResponseDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.EventCompilation;
import ru.practicum.server.event.EventRepository;
import ru.practicum.server.event.LocationRepository;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.server.compilation.CompilationMapper.toCompilationResponseDto;
import static ru.practicum.server.event.EventMapper.toEventResponseDto;


@Service
@Slf4j
public class CompilationPublicServiceImp implements CompilationPublicService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public CompilationPublicServiceImp(EventRepository eventRepository, CompilationRepository compilationRepository,
                                       EventCompilationRepository eventCompilationRepository, CategoryRepository categoryRepository, UserRepository userRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
        this.eventCompilationRepository = eventCompilationRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public CompilationResponseDto getCompilation(long compilationId) {
        checkCompilation(compilationId);
        Compilation compilation = compilationRepository.findById(compilationId).get();
        List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByCompilationId(compilation.getId());
        List<EventResponseDto> events = new ArrayList<>();
        for (EventCompilation eventCompilation : eventCompilationList) {
            eventCompilationRepository.save(new EventCompilation(
                    null,
                    eventCompilation.getEventId(),
                    compilation.getId()));
            Event event = eventRepository.findById(eventCompilation.getEventId()).get();
            events.add(toEventResponseDto(
                    event,
                    userRepository.findById(event.getInitiatorId()).get(),
                    categoryRepository.findById(event.getCategoryId()).get(),
                    locationRepository.findById(event.getLocationId()).get()
            ));
        }
        log.debug("Предоставлены данные событий по подборке ID: " + compilationId);
        return toCompilationResponseDto(compilation, events);
    }

    @Override
    public List<CompilationResponseDto> getAllCompilations(PageRequest pageRequest) {
        List<Compilation> compilations = compilationRepository.findAll(pageRequest).toList();
        List<CompilationResponseDto> compilationResponseDtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventCompilation> eventCompilationList = eventCompilationRepository.findAllByCompilationId(compilation.getId());
            List<EventResponseDto> events = new ArrayList<>();
            for (EventCompilation eventCompilation : eventCompilationList) {
                eventCompilationRepository.save(new EventCompilation(
                        null,
                        eventCompilation.getEventId(),
                        compilation.getId()));
                Event event = eventRepository.findById(eventCompilation.getEventId()).get();
                events.add(toEventResponseDto(
                        event,
                        userRepository.findById(event.getInitiatorId()).get(),
                        categoryRepository.findById(event.getCategoryId()).get(),
                        locationRepository.findById(event.getLocationId()).get()
                ));
            }
            compilationResponseDtoList.add(toCompilationResponseDto(compilation, events));
        }
        log.debug("Предоставлены подборки по запросу");
        return compilationResponseDtoList;
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
