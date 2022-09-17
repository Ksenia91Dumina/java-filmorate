package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserValidations;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationsTest {

    @Test
    void validateBirthdayTest() {
        User user = User.builder()
                .id(1)
                .email("qazwsx@m.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.MAX)
                .build();

        assertThrows(ValidationException.class, () -> UserValidations.validateBirthday(user));
    }

    @Test
    void validateNameTest() {
        User user = User.builder()
                .id(1)
                .email("asd@gmail.com")
                .login("login")
                .name(" ")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();

        UserValidations.validateName(user);
        assertEquals(user.getLogin(), user.getName(), "На месте пустого имени - логин");
    }


    @Test
    void validateLoginTest() {
        User user = User.builder()
                .id(1)
                .email("asd@gmail.com")
                .login("lo  gin")
                .name("name")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();

        assertThrows(ValidationException.class, () -> UserValidations.validateLogin(user));
    }

    @Test
    void validateEmail() {
        User user = User.builder()
                .id(1)
                .email("asdgmail.com")
                .login("login")
                .name("")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();

        assertThrows(ValidationException.class, () -> UserValidations.validateEmail(user));
    }

}