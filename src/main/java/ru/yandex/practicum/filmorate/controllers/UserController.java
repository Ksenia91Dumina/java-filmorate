package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Запрос GET - вывод всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        log.info("Запрос GET - вывод пользователя по номеру id");
        return userService.get(userId);
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрос POST - создание пользователя");
        UserValidations.validateBirthday(user);
        UserValidations.validateName(user);
        UserValidations.validateLogin(user);
        UserValidations.validateEmail(user);
        userService.save(user);
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Запрос PUT - обновление пользователя");
        UserValidations.validateBirthday(user);
        UserValidations.validateName(user);
        UserValidations.validateLogin(user);
        UserValidations.validateEmail(user);
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Запрос PUT - добавление друга для пользователя по номеру id");
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        log.info("Запрос DELETE - удаление друга для пользователя по номеру id");
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public void getFriends(@PathVariable int userId) {
        log.info("Запрос GET - вывод друзей пользователя номеру его id");
        userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public void getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        log.info("Запрос GET - вывод общих друзей для двух пользователей по номерам их id");
        userService.getCommonFriends(userId, otherId);
    }


}