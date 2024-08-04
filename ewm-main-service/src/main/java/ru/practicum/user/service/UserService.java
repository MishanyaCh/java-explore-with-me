package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto inputDto);

    List<UserDto> getUsers(List<Integer> userIds, int from, int size);

    void deleteUser(int id);
}
