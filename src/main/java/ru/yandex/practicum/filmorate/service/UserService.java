package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j

public class UserService {
    private final UserDbStorage userStorage;

    public User get(int userId) {
        try {
            userStorage.getUserById(userId);
            return userStorage.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        try {
            userStorage.getUserById(user.getId());
            return userStorage.updateUser(user);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
    }

    public User addFriend(int userId, int friendId) {
        try {
            userStorage.getUserById(userId);
            userStorage.getUserById(friendId);
            if (userId == friendId) {
                log.error("Попытка добавления в друзья пользователем самого себя");
                throw new ValidationException("Невозможно добавить в друзья себя");
            }
            return userStorage.addFriend(userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id " + userId + "/" + friendId + " не найден");
        }
    }

    public List<User> getFriends(int userId) {
        try {
            userStorage.getUserById(userId);
            return userStorage.getFriends(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
    }

    public User deleteFriend(int userId, int friendId) {
        try {
            userStorage.getUserById(userId);
            userStorage.getUserById(friendId);
            if (userId == friendId) {
                log.error("Попытка удаления из друзей пользователем самого себя");
                throw new ValidationException("Невозможно удалить из друзей себя");
            }
            return userStorage.deleteFriend(userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id " + userId + "/" + friendId + " не найден");
        }
    }


    public List getCommonFriends(int userId, int otherId) {
        try {
            if (userId == otherId) {
                log.error("Попытка вывести список общих друзей пользователем с самим собой");
                throw new ValidationException("Невозможно вывести список общих друзей");
            }
            List<User> commonFriends = userStorage.getCommonFriends(userId, otherId);
            return commonFriends;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id " + userId + "/" + otherId + " не найден");
        }
    }

}