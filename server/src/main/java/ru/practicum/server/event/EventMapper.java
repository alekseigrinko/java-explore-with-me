package ru.practicum.server.event;

import ru.practicum.server.category.model.Category;
import ru.practicum.server.event.dto.EventDto;
import ru.practicum.server.event.dto.EventResponseDto;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.Location;
import ru.practicum.server.user.model.User;

public class EventMapper {
    public static EventDto toEventDto(Event event, Location location) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                event.getState(),
                event.getInitiatorId(),
                event.getCategoryId(),
                event.isPaid(),
                event.isRequestModeration(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getEventDate(),
                new EventDto.LocationDto(
                        location.getLat(),
                        location.getLon()
                ),
                event.getParticipantLimit()
        );
    }

    public static Event toEvent(EventDto eventDto, long locationId) {
        return new Event(
                eventDto.getId(),
                eventDto.getTitle(),
                eventDto.getDescription(),
                eventDto.getAnnotation(),
                eventDto.getState(),
                eventDto.getInitiatorId(),
                eventDto.getCategoryId(),
                eventDto.isPaid(),
                eventDto.isRequestModeration(),
                eventDto.getCreatedOn(),
                eventDto.getPublishedOn(),
                eventDto.getEventDate(),
                locationId,
                eventDto.getParticipantLimit(),
                false
        );
    }

    public static EventResponseDto toEventResponseDto(Event event, User user, Category category, Location location) {
        return new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                event.getState(),
                new EventResponseDto.UserResponseDto(
                        user.getId(),
                        user.getName()
                ),
                new EventResponseDto.CategoryResponseDto(
                        category.getId(),
                        category.getName()
                ),
                event.isPaid(),
                event.isRequestModeration(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getEventDate(),
                new EventResponseDto.LocationResponseDto(
                        location.getLat(),
                        location.getLon()
                ),
                event.getParticipantLimit()
        );
    }
}
