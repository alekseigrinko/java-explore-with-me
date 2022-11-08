package ru.practicum.server.category.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO-класс для возврата данных категории
 * @see ru.practicum.server.category.model.Category
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    @NotNull
    Long id;
    @NotBlank
    String name;
}
