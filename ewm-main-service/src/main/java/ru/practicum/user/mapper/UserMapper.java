package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserMapper {
    User toUser(UserDto inputDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserList(List<User> users);

    UserShortDto toUserShortDto(User user);
}
