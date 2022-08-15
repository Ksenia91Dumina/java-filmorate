package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    public UserDbStorage userStorage = new UserDbStorage();

    public User get(int userId) throws SQLException {
        final User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getUserMap();
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) throws SQLException {
        final User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (userStorage.getUserMap().contains(userId) && userStorage.getUserMap().contains(friendId)) {
            userStorage.addFriend(user, friend);
        }
    }

    public void deleteFriend(int userId, int friendId) throws SQLException {
        final User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public List getFriends(int userId) {
        final User user;
        try {
            user = userStorage.getUserById(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        try {
            return userStorage.getFriends(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List getCommonFriends(int userId, int otherId) throws SQLException {
        final User user = userStorage.getUserById(userId);
        final User friend = userStorage.getUserById(otherId);
        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getCommonFriends(userId, otherId);
    }
}