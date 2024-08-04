package ru.practicum.user.dto;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {
    private Integer id;

    @NotBlank(message = "Поле 'name' не должно быть пустым и состоять из пробелов")
    @Size(min = 2, max = 250, message = "Минимальное количество символов в поле 'name' 2, а максимальное - 250")
    private String name;

    @Email
    @NotBlank(message = "Поле 'email' не должно быть пустым и состоять из пробелов")
    @Size(min = 6, max = 254, message = "Минимальное количество символов в поле 'email' 6, а максимальное - 254")
    private String email;

    public UserDto(Integer idArg, String nameArg, String emailArg) {
        id = idArg;
        name = nameArg;
        email = emailArg;
    }

    @Override
    public String toString() {
        return "UserDto{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + "}";
    }
}
