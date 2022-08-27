package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @BeforeEach
    void init(){
        User user1 = new User();
        User user2 = new User();
        user1.setId(1);
        user1.setEmail("asd@m.ru");
        user1.setLogin("login");
        user1.setName("");
        user1.setBirthday(LocalDate.of(1990, 12, 12));
        user2.setId(2);
        user2.setEmail("zxc@m.ru");
        user2.setLogin("log2");
        user2.setName("name");
        user2.setBirthday(LocalDate.of(1995, 12, 12));
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
        User user3 = new User();
        user3.setId(3);
        user3.setEmail("qazwsx@m.ru");
        user3.setLogin("login3");
        user3.setName("name3");
        user3.setBirthday(LocalDate.of(1995, 12, 12));
        userStorage.save(user3);
        List<User> users = userStorage.getAllUsers();
        assertEquals(3, users.size());
    }

    @Test
    void getByIdTest() {
        User user = userStorage.getUserById(2);
        assertEquals("name", user.getName());
        assertEquals("log2", user.getLogin());
        assertEquals("zxc@m.ru", user.getEmail());
    }

    @Test
    void updateTest() {
        User user = new User(4, "asd@gmail.com", "newLogin", "New name",
                LocalDate.of(1990, 12, 12));
        userStorage.save(user);
        user.setLogin("updateLogin");
        user.setName("UpdateName");
        userStorage.updateUser(user);
        User userToCheck = userStorage.getUserById(4);
        assertEquals("updateLogin", userToCheck.getLogin());
        assertEquals("UpdateName", userToCheck.getName());
    }


    @Test
    void removeTest() {
        userStorage.removeUser(1);
        List<User> users = userStorage.getAllUsers();
        assertEquals(1, users.size());
    }


}