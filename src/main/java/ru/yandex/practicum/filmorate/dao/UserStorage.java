package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    List<User> getUserMap();

    User getUserById(ResultSet rs, int userId) throws SQLException;

    User save(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    HashMap getFriends(int userId) throws SQLException;

    List getCommonFriends(int userId, int otherId);

    User updateUser(User user);
}