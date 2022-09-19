package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class UserControllerTest {
    private final UserController userController;
    private final JdbcTemplate jdbcTemplate;

    private User user1;
    private User user2;

    @Autowired
    public UserControllerTest(UserController userController, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userController = userController;
    }

    @BeforeEach
    void init() {
        String sqlQuery = "DELETE FROM USERS ";
        jdbcTemplate.update(sqlQuery);

        user1 = User.builder()
                .id(1)
                .email("asd@m.ru")
                .login("login")
                .name("")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();

        user2 = User.builder()
                .id(2)
                .email("zxc@m.ru")
                .login("log2")
                .name("name")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();

        userController.createUser(user1);
        userController.createUser(user2);

    }

    @Test
    void getAllUsers() {
        List<User> users = userController.getAllUsers();
        assertEquals(2, users.size());
    }


    @Test
    void createUser() {
        User user3 = User.builder()
                .id(3)
                .email("qazwsx@m.ru")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();
        userController.createUser(user3);
        List<User> users = userController.getAllUsers();
        assertEquals(3, users.size());
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .id(4)
                .email("asd@gmail.com")
                .login("newLogin")
                .name("New name")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();

        userController.createUser(user);
        user.setLogin("updateLogin");
        user.setName("UpdateName");
        userController.updateUser(user);
        User userToCheck = userController.getUser(user.getId());
        assertEquals("updateLogin", userToCheck.getLogin());
        assertEquals("UpdateName", userToCheck.getName());
    }

    @Test
    void addAndGetFriendTest() {
        userController.addFriend(user1.getId(), user2.getId());
        List<User> friends = userController.getFriends(user1.getId());
        assertEquals(1, friends.size());
    }

    @Test
    void deleteFriendTest() {
        User user = User.builder()
                .id(4)
                .email("asd@gmail.com")
                .login("newLogin")
                .name("New name")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();
        userController.createUser(user);
        userController.addFriend(user1.getId(), user2.getId());
        userController.addFriend(user1.getId(), user.getId());
        List<User> friends = userController.getFriends(user1.getId());
        assertEquals(2, friends.size());
        userController.deleteFriend(user1.getId(), user2.getId());
        friends = userController.getFriends(user1.getId());
        assertEquals(1, friends.size());
    }

}