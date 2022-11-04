package ru.practicum.server.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class CategoryDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
