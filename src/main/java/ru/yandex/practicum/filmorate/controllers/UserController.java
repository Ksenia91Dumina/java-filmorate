package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private int uniqueID = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("users")
    public Map<Integer, User> getAllUsers() {
        return Map.copyOf(users);
    }

    @PostMapping("users")
    public User createUser(@RequestBody User user) {
        user.setId(getUniqueID());
        UserValidations.validateBirthday(user);
        UserValidations.validateName(user);
        UserValidations.validateLogin(user);
        UserValidations.validateEmail(user);
        users.put(user.getId(), user);
        log.info("Создан пользователь " + user.getLogin());
        return user;
    }

    @PutMapping("users")
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Изменен пользователь " + user.getLogin());
            return user;
        } else {
            log.info("Пользователь не найден");
            throw new ValidationException("Пользователь не найден");
        }
    }

    public int getUniqueID() {
        return ++uniqueID;
    }

}
