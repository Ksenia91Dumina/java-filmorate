package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    protected UserController userController;
    protected User user1;
    protected User user2;

    @BeforeEach
    void init() {
        userController = new UserController();
        user1 = new User(userController.getUniqueID(), "asd@m.ru", "login", "",
                LocalDate.of(1990, 12, 12));
        user2 = new User(userController.getUniqueID(), "zxc@m.ru", "log", "name",
                LocalDate.of(1995, 12, 12));
        userController.createUser(user1);
        userController.createUser(user2);
    }

    @Test
    void getAllUsers() {
        final Map<Integer, User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 2, "Два пользователя");
        assertEquals(user1, users.get(user1.getId()));
        assertEquals(user2, users.get(user2.getId()));
    }


    @Test
    void createUser() {
        User newUser = new User(userController.getUniqueID(), "qwe@m.ru", "newLogin", "newName",
                LocalDate.of(1995, 12, 12));
        userController.createUser(newUser);
        final Map<Integer, User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 3, "Три пользователя");
        assertEquals(newUser, users.get(newUser.getId()));
    }

    @Test
    void updateUser() {
        user1.setName("New Name");
        userController.updateUser(user1);
        final Map<Integer, User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals("New Name", user1.getName(), "Пользователь обновлен");
    }

    @Test
    void getUniqueID() {
        assertNotEquals(user1.getId(), user2.getId(), "У пользователей разные id");
    }
}