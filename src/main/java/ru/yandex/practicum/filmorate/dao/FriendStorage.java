package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface FriendStorage {

    void addFriend(int userId, int friendId);

    List<Integer> getFriends(int userId);

    void deleteFriend(int userId, int friendId);
}