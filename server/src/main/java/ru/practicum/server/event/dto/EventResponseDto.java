package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.event.model.State;

import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private String annotation;
    private State state;
    private UserResponseDto initiator;
    private CategoryResponseDto category;
    private boolean paid;
    private boolean requestModeration;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private LocalDateTime eventDate;
    LocationResponseDto location;
    long participantLimit;
    long views;
    long confirmedRequests;


    @Getter
    @AllArgsConstructor
    public static class LocationResponseDto {
        private float lat;
        private float lon;
    }

    @Getter
    @AllArgsConstructor
    public static class UserResponseDto {
        private long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class CategoryResponseDto {
        private long id;
        private String name;
    }
}
