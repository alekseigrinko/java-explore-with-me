package ru.practicum.server.category.service;


import ru.practicum.server.category.dto.CategoryDto;

public interface CategoryAdminService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(long categoryId, CategoryDto categoryDto);

    CategoryDto deleteCategory(long categoryId);
}
