package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.service.UserService;
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
    User getUser(@PathVariable int userId) {
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
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public void getFriends(@PathVariable int userId) {
        userService.getFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public void getCommonFriends(@PathVariable int userId, @PathVariable int otherId) {
        userService.getCommonFriends(userId, otherId);
    }


}