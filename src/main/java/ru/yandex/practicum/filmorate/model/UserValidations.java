package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidations {

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
        pattern = Pattern.compile("\\s+");
        matcher = pattern.matcher(user.getLogin());
        if (!matcher.matches()) {
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

}


