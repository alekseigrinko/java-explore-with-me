package ru.practicum.server.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.event.dto.EventResponseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {
    private long id;
    private String title;
    private boolean pinned;
    private List<EventResponseDto> events;
}
