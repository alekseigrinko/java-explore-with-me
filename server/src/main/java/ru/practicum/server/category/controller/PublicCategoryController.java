package ru.practicum.server.category.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.service.publ.CategoryPublicService;

import java.util.List;

/**
 * Публичный контроллер для работы с данными категорий
 * */
@RestController
@RequestMapping(path = "categories")
public class PublicCategoryController {

    /**
     * @see CategoryPublicService - интерефейс публичных методов
     * */
    private final CategoryPublicService categoryPublicService;

    public PublicCategoryController(CategoryPublicService categoryPublicService) {
        this.categoryPublicService = categoryPublicService;
    }

    /**
     * Получение списка действующих категорий
     * */
    @GetMapping
    List<CategoryDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") int from,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        return categoryPublicService.getAllCategories(pageRequest);
    }

    /**
     * Получение данных категории по ID
     * */
    @GetMapping("/{id}")
    CategoryDto getCategory(@PathVariable long id) {
        return categoryPublicService.getCategory(id);
    }


}
