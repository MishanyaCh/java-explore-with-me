package ru.practicum.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/admin/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userServiceArg) {
        userService = userServiceArg;
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserDto inputDto) {
        log.info("Пришел POST /admin/users запрос с телом: {}", inputDto);
        final UserDto createdUser = userService.createUser(inputDto);
        log.info("На POST /admin/users запрос отправлен ответ с телом: {}", createdUser);
        return createdUser;
    }

    @GetMapping()
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Integer> ids,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.info("Пришел GET /admin/users?ids={}&from={}&size={} запрос", ids, from, size);
        final List<UserDto> users = userService.getUsers(ids, from, size);
        log.info("На GET /admin/users?ids={}&from={}&size={} запрос отправлен ответ с размером тела: {}",
                ids, from, size, users.size());
        return users;
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Пришел DELETE /admin/users/{}", userId);
        userService.deleteUser(userId);
        log.info("Пользователь с id={} успешно удалена. Тело ответа отсутствует", userId);
    }
}
