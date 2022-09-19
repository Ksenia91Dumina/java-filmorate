package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User getUserById(int userId) throws SQLException;

    User save(User user);

    List getFriends(int userId) throws SQLException;

    List getCommonFriends(int userId, int otherId);

    User updateUser(User user);

    User addFriend(int userId, int friendId);

    User deleteFriend(int userId, int friendId);
}