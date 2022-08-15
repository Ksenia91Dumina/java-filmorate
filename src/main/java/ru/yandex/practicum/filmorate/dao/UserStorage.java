package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserStorage {
    List<User> getUserMap();

    User getUserById(int userId) throws SQLException;

    User save(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    List getFriends(int userId) throws SQLException;

    List getCommonFriends(int userId, int otherId);

    User updateUser(User user);
}