package ru.practicum.server.category.service.adm;

import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;

/**
 * интерфейс администратора по работе с данными категорий
 * */
public interface CategoryAdminService {

    /**
     * метод сохранения данных пользователя
     * @param newCategoryDto - DTO c данными новой категории
     * @return возвращает DTO-класс с данными категории и ее присвоенного ID
     * @see NewCategoryDto
     * @see CategoryDto
     * */
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    /**
     * метод обнолвения данных пользователя
     * @param categoryDto - DTO c данными для обновления категории
     * @return возвращает DTO-класс с обновленными данными категории
     * @see CategoryDto
     * */
    CategoryDto updateCategory(CategoryDto categoryDto);

    /**
     * метод удаления категории по ID
     * @param categoryId - ID категории
     * */
    CategoryDto deleteCategory(long categoryId);
}
