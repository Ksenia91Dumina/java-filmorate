package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    int uniqueID = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    //не могу понять почему не работает((
    //наверняка опять какую-то мелочь не замечаю((

    //получение всех пользователей
    @GetMapping("users")
    public HashMap<Integer, User> getAllUsers() {
        return users;
    }

    //создание пользователя
    @PostMapping("users")
    public User createUser(@RequestBody User user) {
        user.setId(getUniqueID());
        validateBirthday(user);
        validateName(user);
        validateLogin(user);
        validateEmail(user);
        users.put(user.getId(), user);
        log.info("Создан пользователь " + user.getLogin());
        return user;
    }

    //изменение пользователя
    @PutMapping("users")
    public User updateUser(@RequestBody User user){
        if (!(users.isEmpty())) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Изменен пользователь " + user.getLogin());
                //return user;
            } else {
                log.info("Пользователя не существует");
                return null;
            }
        } return user;
    }

    //валидация - день рождения не в будущем
    void validateBirthday(User user){
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new RuntimeException("Дата рождения не может быть в будущем");
        }
    }

    //если имя пустое, использовать вместо него логин
    void validateName(User user){
        if(user.getName().isEmpty() || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
    }

    //проверка логин на пробелы
    void validateLogin(User user){
        if(user.getLogin().contains(" ")){
            throw new RuntimeException("Логин не может содержать пробелы");
        }
    }

    void validateEmail(User user){
        if(!user.getEmail().contains("@")){
            throw new RuntimeException("Некорректный формат почты");
        }
    }

    public int getUniqueID() {
        return ++uniqueID;
    }

}
