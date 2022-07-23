package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    protected UserController userController;
    protected User user1 = new User();
    protected User user2 = new User();

    @BeforeEach
    void init() {
        userController = new UserController();
        user1.setEmail("asd@m.ru");
        user1.setLogin("login");
        user1.setName("");
        user1.setBirthday(LocalDate.of(1990, 12, 12));
        user2.setEmail("zxc@m.ru");
        user2.setLogin("log2");
        user2.setName("name");
        user2.setBirthday(LocalDate.of(1995, 12, 12));
        userController.createUser(user1);
        userController.createUser(user2);
    }

    @Test
    void getAllUsers() {
        final Collection<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 2, "Два пользователя");
    }


    @Test
    void createUser() {
        User newUser = new User();
        newUser.setBirthday(LocalDate.of(1995, 12, 12));
        newUser.setEmail("qwe@m.ru");
        newUser.setLogin("newLogin");
        newUser.setName("newName");
        userController.createUser(newUser);
        final Collection<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals(users.size(), 3, "Три пользователя");
    }

    @Test
    void updateUser() {
        user1.setName("New Name");
        userController.updateUser(user1);
        final Collection<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals("New Name", user1.getName(), "Пользователь обновлен");
    }

}