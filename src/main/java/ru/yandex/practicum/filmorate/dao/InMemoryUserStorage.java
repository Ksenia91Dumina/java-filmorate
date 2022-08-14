package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {

    public HashMap<Integer, User> userMap = new HashMap<>();

    @Override
    public List<User> getUserMap() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(ResultSet rs, int userId) throws SQLException {
        return null;
    }


    public User getUserById(int userId) {
        User user = new User();
        if (!userMap.isEmpty()) {
            if (userMap.containsKey(userId)) {
                user = userMap.get(userId);
            }
        }
        return user;
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }

    @Override
    public HashMap<Integer, User> getFriends(int userId) {
        User user = getUserById(userId);
        HashMap<Integer, User> allFriends = new HashMap<>();
        Set<Integer> friendsId = user.getFriendIds();
        for (Integer id : friendsId) {
            User friend = getUserById(id);
            allFriends.put(id, friend);
        }
        return allFriends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        HashMap<Integer, User> userFriends = getFriends(userId);
        HashMap<Integer, User> otherUserFriends = getFriends(otherId);
        List<User> commonFriends = userFriends.values().stream()
                .filter(otherUserFriends.values()::contains)
                .collect(Collectors.toList());
        return commonFriends;
    }
}