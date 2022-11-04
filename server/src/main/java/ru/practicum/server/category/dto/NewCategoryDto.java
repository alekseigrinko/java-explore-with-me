package ru.practicum.server.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * DTO-класс для регистрации данных новой категории
 * @see ru.practicum.server.category.model.Category
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank
    private String name;
}
