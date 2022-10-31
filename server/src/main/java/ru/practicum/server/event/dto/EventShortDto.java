package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    long confirmedRequests;
    @NotBlank
    private String eventDate;
    private long id;
    @NotNull
    private UserShortDto initiator;
    @NotBlank
    private String title;
    long views;

}
