package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.NoArgsConstructor;
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


@NoArgsConstructor
@Repository
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select * from USERS";
        List users = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper(User.class));
        return users;
    }


    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public User save(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY) values (?, ?, ?, ?)";

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
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeUser, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "where USER_ID = ?";
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





