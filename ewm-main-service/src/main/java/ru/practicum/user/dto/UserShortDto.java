package ru.practicum.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserShortDto {
    private Integer id;
    private String name;

    public UserShortDto(Integer idArg, String nameArg) {
        id = idArg;
        name = nameArg;
    }

    @Override
    public String toString() {
        return "UserShortDto{" + "id=" + id + ", name='" + name + '\'' + "}";
    }
}
