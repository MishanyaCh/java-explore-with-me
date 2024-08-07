package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(UserDto inputDto) {
        String name = inputDto.getName();
        String email = inputDto.getEmail();
        return new User(null, name, email);
    }

    @Override
    public UserDto toUserDto(User user) {
        Integer id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        return new UserDto(id, name, email);
    }

    @Override
    public List<UserDto> toUserList(List<User> users) {
        List<UserDto> result = new ArrayList<>();
        if (users.isEmpty()) {
            return result;
        }
        for (User user : users) {
            UserDto userDto = toUserDto(user);
            result.add(userDto);
        }
        return result;
    }

    @Override
    public UserShortDto toUserShortDto(User user) {
        Integer id = user.getId();
        String name = user.getName();
        return new UserShortDto(id, name);
    }
}
