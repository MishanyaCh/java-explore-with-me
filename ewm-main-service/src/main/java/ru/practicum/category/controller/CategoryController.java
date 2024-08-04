package ru.practicum.category.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryServiceArg) {
        categoryService = categoryServiceArg;
    }

    @PostMapping(path = "/admin/categories")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto inputDto) {
        log.info("Пришел POST /admin/categories запрос с телом: {}", inputDto);
        final CategoryDto createdCategory = categoryService.createCategory(inputDto);
        log.info("На POST /admin/categories запрос отправлен ответ с телом: {}", createdCategory);
        return createdCategory;
    }

    @PatchMapping(path = "/admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId, @Valid @RequestBody NewCategoryDto inputDto) {
        log.info("Пришел PATCH /admin/categories/{} запрос с телом: {}", catId, inputDto);
        final CategoryDto updatedCategory = categoryService.updateCategory(catId, inputDto);
        log.info("На PATCH /admin/categories/{} запрос отправлен ответ с телом: {}", catId, updatedCategory);
        return updatedCategory;
    }

    @DeleteMapping(path = "/admin/categories/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("Пришел DELETE /admin/categories/{} запрос", catId);
        categoryService.deleteCategory(catId);
        log.info("Категория с id={} успешно удалена. Тело ответа отсутствует", catId);
    }

    @GetMapping(path = "/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Integer catId) {
        log.info("Пришел GET /categories/{}", catId);
        final CategoryDto category = categoryService.getCategory(catId);
        log.info("На GET /categories/{} запрос отправлен ответ с телом: {}", catId, category);
        return category;
    }

    @GetMapping(path = "/categories")
    public List<CategoryDto> getCategories(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("Пришел GET /categories?from={}&size={} запрос", from, size);
        final List<CategoryDto> categories = categoryService.getCategories(from, size);
        log.info("На GET /categories?from={}&size={} запрос отправлен ответ с размером тела: {}",
                from, size, categories.size());
        return categories;
    }
}
