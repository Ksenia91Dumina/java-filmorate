package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService {

    private UserDbStorage userStorage;
    private FriendDbStorage friends;

    @Autowired
    public UserService(UserDbStorage userStorage, FriendDbStorage friends ) {
        this.userStorage = userStorage;
        this.friends = friends;
    }


    public User get(int userId) {
        final User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
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
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) throws SQLException {
            friends.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) throws SQLException {
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