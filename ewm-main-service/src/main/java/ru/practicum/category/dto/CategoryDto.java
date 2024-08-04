package ru.practicum.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Integer id;
    private String name;

    public CategoryDto(Integer idArg, String nameArg) {
        id = idArg;
        name = nameArg;
    }

    @Override
    public String toString() {
        return "CategoryDto{" + "id=" + id + ", name='" + name + '\'' + "}";
    }
}
