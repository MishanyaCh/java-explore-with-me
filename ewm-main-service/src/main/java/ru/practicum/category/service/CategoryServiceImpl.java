package ru.practicum.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectAlreadyExistException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.util.Pages;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapperArg, CategoryRepository categoryRepositoryArg,
                               EventRepository eventRepositoryArg) {
        categoryMapper = categoryMapperArg;
        categoryRepository = categoryRepositoryArg;
        eventRepository = eventRepositoryArg;
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto inputDto) {
        Category newCategory = categoryMapper.toCategory(inputDto);
        String name = newCategory.getName();
        if (categoryRepository.existsByName(name)) {
            String message = String.format("Категория с именем '%s' уже существует! " +
                    "При добавлении новой категории дублирование имен не допускается!", name);
            throw new ObjectAlreadyExistException(message);
        }
        Category createdCategory = categoryRepository.save(newCategory);
        return categoryMapper.toCategoryDto(createdCategory);
    }

    @Override
    public CategoryDto updateCategory(int id, NewCategoryDto inputDto) {
        Optional<Category> optionalSavedCategory = categoryRepository.findById(id);
        if (optionalSavedCategory.isEmpty()) {
            String message = String.format("Категория с id=%d не найдена! Обновление данных невозможно", id);
            throw new ObjectNotFoundException(message);
        }
        Category savedCategory = optionalSavedCategory.get();

        Category category = categoryMapper.toCategory(inputDto);
        String updatedName = category.getName();
        savedCategory.setName(updatedName);
        try {
            Category updatedCategory = categoryRepository.save(savedCategory);
            return categoryMapper.toCategoryDto(updatedCategory);
        } catch (RuntimeException exc) {
            String message = String.format("Категория с именем '%s' уже существует! " +
                    "Невозможно выполнить обновление в БД!", updatedName);
            throw new ObjectAlreadyExistException(message);
        }
    }

    @Override
    public CategoryDto getCategory(int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            String message = String.format("Категория с id=%d не найдена!", id);
            throw new ObjectNotFoundException(message);
        }
        Category category = optionalCategory.get();
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = Pages.getPage(from, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();
        return categoryMapper.toCategoryDtoList(categories);
    }

    @Override
    public void deleteCategory(int id) {
        boolean isCategoryExist = categoryRepository.existsById(id);
        if (!isCategoryExist) {
            String message = String.format("Категория с id=%d не найдена! Операция удаления невозможна", id);
            throw new ObjectNotFoundException(message);
        }
        long eventsCount = eventRepository.countEventsByCategoryId(id);
        if (eventsCount > 0) {
            String message = String.format("Невозможно удалить категорию с id=%d, так как с данной категорией " +
                    "связано '%d' событий", id, eventsCount);
            throw new ConflictException(message);
        }
        categoryRepository.deleteById(id);
    }
}
