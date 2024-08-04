package ru.practicum.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ObjectAlreadyExistException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;
import ru.practicum.util.Pages;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapperArg, UserRepository userRepositoryArg) {
        userMapper = userMapperArg;
        userRepository = userRepositoryArg;
    }

    @Override
    public UserDto createUser(UserDto inputDto) {
        User newUser = userMapper.toUser(inputDto);
        String email = newUser.getEmail();
        if (userRepository.existsByEmail(email)) {
            String message = String.format("Пользователь с почтой '%s' уже зарегистрирован! " +
                    "При добавлении нового пользователя дублирование почтовых ящиков не допускается!", email);
            throw new ObjectAlreadyExistException(message);
        }
        User savedUser = userRepository.save(newUser);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers(List<Integer> userIds, int from, int size) {
        Pageable pageable = Pages.getPage(from, size);
        List<User> users;
        if (userIds == null) {
            Page<User> userPage = userRepository.findAll(pageable);
            users = userPage.getContent();
        } else {
            users = userRepository.findUsersByIdIn(userIds, pageable);
        }
        return userMapper.toUserList(users);
    }

    @Override
    public void deleteUser(int id) {
        boolean isUserExist = userRepository.existsById(id);
        if (!isUserExist) {
            String message = String.format("Пользователь с id=%d не найден! Операция удаления невозможна", id);
            throw new ObjectNotFoundException(message);
        }
        userRepository.deleteById(id);
    }
}
