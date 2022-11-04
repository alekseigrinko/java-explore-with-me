package ru.practicum.server.category;

import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;
import ru.practicum.server.category.model.Category;

/**
 * Маппер для работы с моделями и DTO-классами пакета category
 * */
public class CategoryMapper {

    /**
     * Метод конвертации данных категории из репозитория в DTO-класс
     * */
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    /**
     * Метод конвертации данных категории из DTO-класса в модель Category
     * */
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName()
        );
    }
}
