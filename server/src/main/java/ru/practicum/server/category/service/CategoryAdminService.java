package ru.practicum.server.category.service;

import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;

public interface CategoryAdminService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto deleteCategory(long categoryId);
}
