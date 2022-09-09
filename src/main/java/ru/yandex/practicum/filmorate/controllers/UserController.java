package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserService userService;
    private static int uniqueID = 1;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.get(userId);
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        user.setId(++uniqueID);
        UserValidations.validateBirthday(user);
        UserValidations.validateName(user);
        UserValidations.validateLogin(user);
        UserValidations.validateEmail(user);
        userService.save(user);
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
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