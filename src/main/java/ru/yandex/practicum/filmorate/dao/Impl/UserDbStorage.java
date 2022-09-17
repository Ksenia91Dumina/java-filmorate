package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component("UserDbStorage")
@Primary
@RequiredArgsConstructor

public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser);
        return users;
    }

    @Override
    public User save(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        log.info("Добавлен пользователь с id = " + user.getId());
        return user;
    }

    @Override
    public User getUserById(int userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, userId);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public User addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Добавлен друг с id = " + friendId);
        return getUserById(userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sqlQuery = "SELECT * FROM USERS u WHERE u.USER_ID IN" +
                "(SELECT FRIEND_ID FROM FRIENDSHIP f WHERE f.USER_ID = ?)";
        List<User> friends = jdbcTemplate.query(sqlQuery, this::makeUser, userId);
        return friends;
    }

    @Override
    public User deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("Удален друг с id = " + friendId);
        return getUserById(userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        String sql = "SELECT USERS.* FROM USERS u " +
                "INNER JOIN FRIENDSHIP f ON u.USER_ID = f.FRIEND_ID " +
                "WHERE f.USER_ID = ? " +
                "AND f.FRIEND_ID IN (" +
                "    SELECT FRIEND_ID " +
                "    FROM FRIENDSHIP f " +
                "    WHERE f.USER_ID = ?)";
        List<User> commonFriends = jdbcTemplate.query(sql, this::makeUser, userId, otherId);
        return commonFriends;
    }

    public void removeUser(int userId) {
        String sqlQuery = "DELETE FROM USERS where USER_ID = ?";
        int result = jdbcTemplate.update(sqlQuery, userId);
        if (result > 0) {
            log.info("Удален пользователь с id = " + userId);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}





