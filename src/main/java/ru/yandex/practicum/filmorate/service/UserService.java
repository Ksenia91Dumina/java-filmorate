package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Service
public class UserService {
    @Autowired
    InMemoryUserStorage userStorage;

    public User get(int userId) {
        final User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.get(userId);
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public void addFriend(int userId, int friendId) {
        final User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        if(userStorage.getUserMap().contains(userId) && userStorage.getUserMap().contains(friendId)){
            userStorage.addFriend(user, friend);
        }
    }

    public void deleteFriend(int userId, int friendId) {
        final User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public HashMap getFriends(int userId) {
        final User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getFriends(userId);
    }

    public HashMap getMutualFriends(int userId, int otherId) {
        final User user = userStorage.get(userId);
        final User friend = userStorage.get(otherId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getMutualFriends(userId, otherId);
    }
}
