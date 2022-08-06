package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationsTest {

    protected UserController userController;

    @Test
    void validateBirthdayTest() {
        User user = new User();
        user.setBirthday(LocalDate.MAX);
        assertThrows(ValidationException.class, () -> UserValidations.validateBirthday(user));
    }

    @Test
    void validateNameTest() {
        User user = new User();
        user.setEmail("asd@gmail.com");
        user.setLogin("login");
        user.setName(" ");
        user.setBirthday(LocalDate.of(1990, 12, 12));
        UserValidations.validateName(user);
        assertEquals(user.getLogin(), user.getName(), "На месте пустого имени - логин");
    }


    @Test
    void validateLoginTest() {
        User user = new User();
        user.setEmail("asd@gmail.com");
        user.setLogin("lo  gin");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 12, 12));
        assertThrows(ValidationException.class, () -> UserValidations.validateLogin(user));
    }

    @Test
    void validateEmail() {
        User user = new User();
        user.setEmail("asdgmail.com");
        user.setLogin("login");
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 12, 12));
        assertThrows(ValidationException.class, () -> UserValidations.validateEmail(user));
    }

}