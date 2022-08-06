package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    InMemoryUserStorage userStorage;

    public User get(int userId) {
        final User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getUserById(userId);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getUserMap();
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        final User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (userStorage.getUserMap().contains(userId) && userStorage.getUserMap().contains(friendId)) {
            userStorage.addFriend(user, friend);
        }
    }

    public void deleteFriend(int userId, int friendId) {
        final User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public HashMap getFriends(int userId) {
        final User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getFriends(userId);
    }

    public List getCommonFriends(int userId, int otherId) {
        final User user = userStorage.getUserById(userId);
        final User friend = userStorage.getUserById(otherId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getCommonFriends(userId, otherId);
    }
}
