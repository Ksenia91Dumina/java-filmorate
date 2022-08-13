package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryUserStorageTest {
    public InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    public User user1 = new User();
    public User user2 = new User();
    public int uniqueId = 1;

    @BeforeEach
    void init() {
        user1.setId(++uniqueId);
        user1.setEmail("asd@m.ru");
        user1.setLogin("login");
        user1.setName("");
        user1.setBirthday(LocalDate.of(1990, 12, 12));
        user2.setId(++uniqueId);
        user2.setEmail("zxc@m.ru");
        user2.setLogin("log2");
        user2.setName("name");
        user2.setBirthday(LocalDate.of(1995, 12, 12));
        inMemoryUserStorage.save(user1);
        inMemoryUserStorage.save(user2);
    }

    @Test
    void getUserMapTest() {
        final Collection<User> users = inMemoryUserStorage.getUserMap();
        assertNotNull(users);
        assertEquals(2, users.size(), "Два пользователя");
    }

    @Test
    void getUserByIdTest() {
        User userToCheck = inMemoryUserStorage.getUserById(2);
        System.out.println(user2.getName());
        System.out.println(userToCheck.getName());
        System.out.println(inMemoryUserStorage.getUserById(2).getName());
        assertEquals(user2.getName(), userToCheck.getName(), "Имена равны");
        assertEquals(user2, userToCheck, "Пользователи равны");
    }

    @Test
    void saveTest() {
        User newUser = new User();
        newUser.setId(++uniqueId);
        newUser.setBirthday(LocalDate.of(1995, 12, 12));
        newUser.setEmail("qwe@m.ru");
        newUser.setLogin("newLogin");
        newUser.setName("newName");
        inMemoryUserStorage.save(newUser);
        final Collection<User> users = inMemoryUserStorage.getUserMap();
        assertNotNull(users);
        assertEquals(3, users.size(), "Три пользователя");
    }

    @Test
    void updateUserTest() {
        user1.setName("New Name");
        inMemoryUserStorage.updateUser(user1);
        final Collection<User> users = inMemoryUserStorage.getUserMap();
        assertNotNull(users);
        assertEquals("New Name", user1.getName(), "Пользователь обновлен");
    }

    @Test
    void addFriendTest() {
        inMemoryUserStorage.addFriend(user1, user2);
        assertEquals(1, user1.getFriendIds().size(), "Друг добавлен");
    }

    @Test
    void deleteFriendTest() {
        inMemoryUserStorage.deleteFriend(user1, user2);
        assertEquals(0, user1.getFriendIds().size(), "Друг удален");
    }

    @Test
    void getFriendsTest() {
        inMemoryUserStorage.addFriend(user1, user2);
        HashMap<Integer, User> friends = inMemoryUserStorage.getFriends(user1.getId());
        assertEquals(1, friends.size(), "У User1 - 1 друг");
    }

    @Test
    void getCommonFriendsTest() {
        User user3 = new User();
        user3.setId(++uniqueId);
        User user4 = new User();
        user4.setId(++uniqueId);
        User user5 = new User();
        user5.setId(++uniqueId);
        inMemoryUserStorage.save(user3);
        inMemoryUserStorage.save(user4);
        inMemoryUserStorage.save(user5);
        inMemoryUserStorage.addFriend(user1, user3);
        inMemoryUserStorage.addFriend(user3, user2);
        inMemoryUserStorage.addFriend(user1, user4);
        inMemoryUserStorage.addFriend(user4, user2);
        inMemoryUserStorage.addFriend(user4, user5);
        assertEquals(2, inMemoryUserStorage.getCommonFriends(user4.getId(), user3.getId()).size(),
                "2 общих друга");
        assertEquals(2, inMemoryUserStorage.getCommonFriends(user1.getId(), user2.getId()).size(),
                "2 общих друга");
        assertEquals(1, inMemoryUserStorage.getCommonFriends(user5.getId(), user2.getId()).size(),
                "1 общий друг");
    }
}