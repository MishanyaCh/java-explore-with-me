package ru.practicum.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewCategoryDto {
    @NotBlank(message = "Поле 'name' не должно быть пустым и состоять из пробелов")
    @Length(min = 1, max = 50, message = "Минимальное количество символов в поле 'name' 1, а максимальное - 50")
    private String name;

    public NewCategoryDto(@JsonProperty("name") String nameArg) {
        name = nameArg;
    }

    @Override
    public String toString() {
        return "NewCategoryDto{" + "name='" + name + '\'' + "}";
    }
}
