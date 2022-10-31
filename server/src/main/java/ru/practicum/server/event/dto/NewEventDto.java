package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    private String annotation;
    @NotNull
    private long category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    Location location;
    private boolean paid;
    long participantLimit;
    private boolean requestModeration;
    @NotBlank
    private String title;

}
