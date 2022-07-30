package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    Collection<User> getUserMap();

    User getUserById(int userId);

    User save(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    HashMap getFriends(int userId);

    List getCommonFriends(int userId, int otherId);

    User updateUser(User user);
}
