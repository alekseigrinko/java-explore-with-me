package ru.practicum.server.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank
    private String title;
    private boolean pinned;
    private List<Integer> events;
}
