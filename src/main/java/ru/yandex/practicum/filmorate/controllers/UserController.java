package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    private static int uniqueID = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping()
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable int userId){
        return userService.get(userId);
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        user.setId(++uniqueID);
        UserValidations.validateBirthday(user);
        UserValidations.validateName(user);
        UserValidations.validateLogin(user);
        UserValidations.validateEmail(user);
        //users.put(user.getId(), user);
        User savedUser = userService.save(user);
        log.info("Создан пользователь " + user.getLogin());
        return savedUser;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) throws ValidationException{
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Изменен пользователь " + user.getLogin());
            return user;
        } else {
            log.info("Пользователь не найден");
            throw new ValidationException("Пользователь не найден");
        }
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId){
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId){
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public void getFriends(@PathVariable int userId){
        userService.getFriends(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public void getMutualFriends(@PathVariable int userId, @PathVariable int otherId){
        userService.getMutualFriends(userId, otherId);
    }

}
