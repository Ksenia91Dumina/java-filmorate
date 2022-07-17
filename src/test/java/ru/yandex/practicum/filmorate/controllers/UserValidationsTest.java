package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationsTest {

    protected UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    void validateBirthdayTest() {
        User user = new User();
        user.setBirthday(LocalDate.MAX);
        assertThrows(ValidationException.class, () -> UserValidations.validateBirthday(user));
    }

    @Test
    void validateNameTest() {
        User user = new User(userController.getUniqueID(), "asd@gmail.com", "login", " ",
                LocalDate.of(1990, 12, 12));
        UserValidations.validateName(user);
        assertEquals(user.getLogin(), user.getName(), "На месте пустого имени - логин");
    }


    @Test
    void validateLoginTest() {
        User user = new User(userController.getUniqueID(), "asd@gmail.com", "lo  gin", "",
                LocalDate.of(1990, 12, 12));
        assertThrows(ValidationException.class, () -> UserValidations.validateLogin(user));
    }

    @Test
    void validateEmail() {
        User user = new User(userController.getUniqueID(), "asdgmail.com", "login", "",
                LocalDate.of(1990, 12, 12));
        assertThrows(ValidationException.class, () -> UserValidations.validateEmail(user));
    }

}