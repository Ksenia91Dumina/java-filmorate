package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.FriendDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j

public class UserService {

    private final UserDbStorage userStorage;
    private final FriendDbStorage friends;

    public User get(int userId) {
        final User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        }
        return userStorage.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        if (!(getAllUsers().isEmpty())) {
            if (!getAllUsers().contains(user)) {
                log.info("Пользователя не существует");
                throw new NotFoundException("Пользователь с id " + user.getId() + "не найден");
            }
            userStorage.updateUser(user);
        }
        return user;
    }

    public void addFriend(int userId, int friendId) {
        List<User> users = getAllUsers();
        if (!users.contains(get(userId))) {
            log.info("Пользователя с id " + userId + "не существует");
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        } else if (!users.contains(get(friendId))) {
            log.info("Пользователя с id " + friendId + "не существует");
            throw new NotFoundException("Пользователь с id " + friendId + "не найден");
        }
        friends.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        List<User> users = getAllUsers();
        if (!users.contains(get(userId))) {
            log.info("Пользователя с id " + userId + "не существует");
            throw new NotFoundException("Пользователь с id " + userId + "не найден");
        } else if (!users.contains(get(friendId))) {
            log.info("Пользователя с id " + friendId + "не существует");
            throw new NotFoundException("Пользователь с id " + friendId + "не найден");
        }
        friends.deleteFriend(userId, friendId);
    }

    public List getFriends(int userId) {
        User user = userStorage.getUserById(userId);
        List<Integer> friendsIds = friends.getFriends(userId);
        if (friendsIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            return friendsIds.stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());
        }
    }

    public List getCommonFriends(int userId, int otherId) throws SQLException {
        List<User> userFriends = getFriends(userId);
        List<User> otherUserFriends = getFriends(otherId);
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }

}