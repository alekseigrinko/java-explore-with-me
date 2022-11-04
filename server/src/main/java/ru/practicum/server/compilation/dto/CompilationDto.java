package ru.practicum.server.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO-класс для возврата данных подборки событий
 * @see ru.practicum.server.compilation.model.Compilation
 * @see ru.practicum.server.compilation.model.EventCompilation
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    @NotNull
    private long id;
    @NotBlank
    private String title;
    @NotNull
    private boolean pinned;
    /**
     * Список DTO событий, полученных по привязке через репозиторий
     * @see ru.practicum.server.compilation.model.EventCompilation
     * @see EventShortDto
     * */
    private List<EventShortDto> events;
}
