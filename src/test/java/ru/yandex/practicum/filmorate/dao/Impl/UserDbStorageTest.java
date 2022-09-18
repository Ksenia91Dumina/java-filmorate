package ru.yandex.practicum.filmorate.dao.Impl;

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
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorageTest(UserDbStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @BeforeEach
    void init() {
        String sqlQuery = "DELETE FROM USERS ";
        jdbcTemplate.update(sqlQuery);

        User user1 = User.builder()
                .id(1)
                .email("asd@m.ru")
                .login("login")
                .name("")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();

        User user2 = User.builder()
                .id(2)
                .email("zxc@m.ru")
                .login("log2")
                .name("name")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();

        userStorage.save(user1);
        userStorage.save(user2);

    }


    @Test
    void getAllUsersTest() {
        List<User> users = userStorage.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void saveTest() {
        User user3 = User.builder()
                .id(3)
                .email("qazwsx@m.ru")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(1995, 12, 12))
                .build();
        userStorage.save(user3);
        List<User> users = userStorage.getAllUsers();
        assertEquals(3, users.size());
    }


    @Test
    void updateTest() {
        User user = User.builder()
                .id(4)
                .email("asd@gmail.com")
                .login("newLogin")
                .name("New name")
                .birthday(LocalDate.of(1990, 12, 12))
                .build();

        userStorage.save(user);
        user.setLogin("updateLogin");
        user.setName("UpdateName");
        userStorage.updateUser(user);
        User userToCheck = userStorage.getUserById(user.getId());
        assertEquals("updateLogin", userToCheck.getLogin());
        assertEquals("UpdateName", userToCheck.getName());
    }

}