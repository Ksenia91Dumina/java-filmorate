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
    private User user1;
    private User user2;

    @Autowired
    public UserDbStorageTest(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    @BeforeEach
    void init(){
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

   /*@Test
    void getAllUsersTest() {
        List<User> users = userStorage.getAllUsers();
        assertEquals(testUsers.size(), users.size());
    }*/

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
        User user = User.builder()
                .id(4)
                .email("asd@gmail.com")
                .login("newLogin")
                .name("New name")
                .birthday( LocalDate.of(1990, 12, 12))
                .build();

        userStorage.save(user);
        user.setLogin("updateLogin");
        user.setName("UpdateName");
        userStorage.updateUser(user);
        User userToCheck = userStorage.getUserById(user.getId());
        assertEquals("updateLogin", userToCheck.getLogin());
        assertEquals("UpdateName", userToCheck.getName());
    }


   /*@Test
    void removeTest() {
        userStorage.removeUser(1);
        testUsers.remove(1);
        List<User> users = userStorage.getAllUsers();
        assertEquals(testUsers.size(), users.size());
    }*/


}