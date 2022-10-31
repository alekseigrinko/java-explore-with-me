package ru.practicum.server.event;

import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.dto.EventShortDto;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.Location;
import ru.practicum.server.event.model.State;
import ru.practicum.server.user.dto.UserShortDto;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto, long userId) {
        return new Event(
                null,
                newEventDto.getTitle(),
                newEventDto.getDescription(),
                newEventDto.getAnnotation(),
                State.PENDING,
                userId,
                newEventDto.getCategory(),
                newEventDto.isPaid(),
                newEventDto.isRequestModeration(),
                LocalDateTime.now(),
                null,
                LocalDateTime.parse(newEventDto.getEventDate(), formatter),
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon(),
                newEventDto.getParticipantLimit(),
                false
        );
    }

    public static EventFullDto toEventFullDto(Event event, User user, Category category,
                                              long views, long confirmedRequests) {
        return new EventFullDto(
                event.getAnnotation(),
                new CategoryDto(
                        category.getId(),
                        category.getName()
                ),
                confirmedRequests,
                event.getCreatedOn().format(formatter),
                event.getDescription(),
                event.getEventDate().format(formatter),
                event.getId(),
                new UserShortDto(
                        user.getId(),
                        user.getName()
                ),
                new Location(
                        event.getLatLocation(),
                        event.getLonLocation()
                ),
                event.isPaid(),
                event.getParticipantLimit(),
                getPublished(event),
                event.isRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                views
        );
    }

    public static EventShortDto toEventShortDto(Event event, User user, Category category,
                                                long views, long confirmedRequests) {
        return new EventShortDto(
                event.getAnnotation(),
                new CategoryDto(
                        category.getId(),
                        category.getName()
                ),
                confirmedRequests,
                event.getEventDate().format(formatter),
                event.getId(),
                new UserShortDto(
                        user.getId(),
                        user.getName()
                ),
                event.getTitle(),
                views
        );
    }

    static String getPublished(Event event) {
        if (event.getPublishedOn() == null) {
            return null;
        } else {
            return event.getPublishedOn().format(formatter);
        }
    }
}
