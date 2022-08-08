package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UserValidations {

    public static UserController userController = new UserController(new UserService());
    private static Pattern pattern;
    private static Matcher matcher;

    public static void validateBirthday(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public static void validateName(User user) {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static void validateLogin(User user) {
        pattern = Pattern.compile("(.+)\\s(.+)");
        matcher = pattern.matcher(user.getLogin());
        if (matcher.matches()) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
    }

    public static void validateEmail(User user) {
        pattern = Pattern.compile("^(.+)@(.+)$");
        matcher = pattern.matcher(user.getEmail());
        if (!matcher.matches()) {
            throw new ValidationException("Некорректный формат почты");
        }
    }

    public static void validateForUpdateUser(User user) {
        Collection<User> users = userController.getAllUsers();
        if (!(users.isEmpty())) {
            if (!users.contains(user)) {
                log.info("Пользователя не существует");
                throw new ValidationException("Пользователя не существует");
            }
        }
    }

}


