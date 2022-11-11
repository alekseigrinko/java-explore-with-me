package ru.practicum.server.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    @NotNull
    long id;
    @NotBlank
    String title;
    @NotNull
    boolean pinned;
    /**
     * Список DTO событий, полученных по привязке через репозиторий
     * @see ru.practicum.server.compilation.model.EventCompilation
     * @see EventShortDto
     * */
    List<EventShortDto> events;
}
