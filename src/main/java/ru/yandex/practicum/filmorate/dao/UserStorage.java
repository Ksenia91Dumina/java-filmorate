package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {
    Collection<Integer> getUserMap();

    User get(int userId);

    User save(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

    HashMap getFriends(int userId);

    HashMap getMutualFriends(int userId, int otherId);
}
