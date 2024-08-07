package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public Category toCategory(NewCategoryDto inputDto) {
        String name = inputDto.getName();
        return new Category(null, name);
    }

    @Override
    public CategoryDto toCategoryDto(Category category) {
        Integer id = category.getId();
        String name = category.getName();
        return new CategoryDto(id, name);
    }

    @Override
    public List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();
        if (categories.isEmpty()) {
            return result;
        }
        for (Category category : categories) {
            CategoryDto catDto = toCategoryDto(category);
            result.add(catDto);
        }
        return result;
    }
}
