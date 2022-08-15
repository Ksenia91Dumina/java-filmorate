package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserService userService;
    private static int uniqueID = 1;

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable int userId)  {
        try {
            return userService.get(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        user.setId(++uniqueID);
        UserValidations.validateBirthday(user);
        UserValidations.validateName(user);
        UserValidations.validateLogin(user);
        UserValidations.validateEmail(user);
        userService.save(user);
        log.info("Создан пользователь " + user.getLogin());
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        UserValidations.validateForUpdateUser(user);
        userService.updateUser(user);
        log.info("Изменен пользователь " + user.getLogin());
        return user;
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        try {
            userService.addFriend(userId, friendId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        try {
            userService.deleteFriend(userId, friendId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/users/{userId}/friends")
    public void getFriends(@PathVariable int userId) {
        userService.getFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public void getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        try {
            userService.getCommonFriends(userId, otherId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}