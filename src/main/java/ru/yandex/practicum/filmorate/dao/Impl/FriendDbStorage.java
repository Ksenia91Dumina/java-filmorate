package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Repository
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
            log.info("Добавлен друг с id = " + friendId);
        } catch (Exception e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<Integer> getFriends(int userId) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, int.class, userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        try {
            int updatedRowsCount = jdbcTemplate.update(sqlQuery, userId, friendId);
            if (updatedRowsCount > 0) {
                log.info("Удален друг с id = " + friendId);
            }
        } catch (Exception e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
