package ru.practicum.server.category.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;
import ru.practicum.server.category.service.adm.CategoryAdminService;

/**
 * Контроллер администратора для работы с данными категорий
 * */
@RestController
@Slf4j
@RequestMapping(path = "admin/categories")
public class AdminCategoryController {

    /**
     * @see CategoryAdminService - интерефейс методов администратора
     * */
    private final CategoryAdminService categoryAdminService;

    public AdminCategoryController(CategoryAdminService categoryAdminService) {
        this.categoryAdminService = categoryAdminService;
    }

    /**
     * Создание данных новой категории
     * */
    @PostMapping
    CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.debug("Получен запрос на удаление категории");
        return categoryAdminService.addCategory(newCategoryDto);
    }

    /**
     * Обновление данных пользователя
     * */
    @PatchMapping
    CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.debug("Переданы данные для регистрации категории");
        return categoryAdminService.updateCategory(categoryDto);
    }

    /**
     * Удаление категории по ID
     * */
    @DeleteMapping("/{id}")
    CategoryDto deleteCategory(@PathVariable long id) {
        log.debug("Получен запрос на удаление категории");
        return categoryAdminService.deleteCategory(id);
    }

}
