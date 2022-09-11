package ru.yandex.practicum.filmorate.dao.Impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase

class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private List<User> testUsers = new ArrayList<>();
    private User user1 = new User();
    private User user2 = new User();

    @Autowired
    public UserDbStorageTest(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    @BeforeEach
    void init(){
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
        testUsers.add(user1);
        testUsers.add(user2);
    }

    @AfterEach
    public void clearUsers(){
        List<User> users = userStorage.getAllUsers();
        users.clear();
        testUsers.clear();
    }

    //Если запускать по отдельности getAllUsersTest и removeTest - работают((

   @Test
    void getAllUsersTest() {
        List<User> users = userStorage.getAllUsers();
        assertEquals(testUsers.size(), users.size());
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
        testUsers.add(user3);
        List<User> users = userStorage.getAllUsers();
        assertEquals(testUsers.size(), users.size());
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
        User userToCheck = userStorage.getUserById(user.getId());
        assertEquals("updateLogin", userToCheck.getLogin());
        assertEquals("UpdateName", userToCheck.getName());
    }


   @Test
    void removeTest() {
        userStorage.removeUser(1);
        testUsers.remove(1);
        List<User> users = userStorage.getAllUsers();
        assertEquals(testUsers.size(), users.size());
    }


}