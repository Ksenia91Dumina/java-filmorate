package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@Primary
@Getter
@Setter
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser);
        return users;
    }


    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
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
        user.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        log.info("Добавлен пользователь с id = " + user.getId());
        return user;
    }

    @Override
    public User getUserById(int userId) {
        User user = new User();
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        try {
            List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, userId);
            user = users.get(0);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
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
    public List getFriends(int userId) throws SQLException {
        final String sqlQuery = "SELECT FRIEND_ID FROM USERS WHERE USER_ID = ?";
        List<Integer> userFriends = jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
        return userFriends;
    }


    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        String sql = "SELECT * FROM USERS " +
                "INNER JOIN FRIENDSHIP ON USERS.USER_ID = FRIENDSHIP.FRIEND_ID " +
                "WHERE FRIENDSHIP.USER_ID = ? " +
                "AND FRIEND_ID IN (" +
                "    SELECT FRIEND_ID " +
                "    FROM FRIENDSHIP " +
                "    WHERE FRIENDSHIP.USER_ID = ?)";
        return jdbcTemplate.query(sql, this::makeUser, userId, otherId);
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





