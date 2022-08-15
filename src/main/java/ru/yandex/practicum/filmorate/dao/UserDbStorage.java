package ru.yandex.practicum.filmorate.dao;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@NoArgsConstructor
@Repository
@Primary
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUserMap() {
        String sqlQuery = "select * from USERS";
        List users = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper(User.class));
        return users;
    }


    public User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    @Override
    public User save(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";

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
        return user;
    }

    @Override
    public User getUserById(int userId) throws SQLException {
        final String sqlQuery = "select * from USERS where USER_ID = ?";
        User user = jdbcTemplate.queryForObject(sqlQuery, this::makeUser, userId);
        return user;
    }

    @Override
    public User updateUser (User user){
        String sqlQuery = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?" +
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
    public void addFriend(User user, User friend) {
        String sqlQuery = "insert into FRIENDSHIP (USER1_ID, USER2_ID, STATUS) values (?,?,?)";
        Boolean trueFriend = false;
        Set<Integer> userFriendsIds = user.getFriendIds();
        if (userFriendsIds.contains(friend.getId())) {
            trueFriend = true;
        }
        Boolean finalTrueFriend = trueFriend;
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, user.getId());
            stmt.setInt(2, friend.getId());
            stmt.setBoolean(3, finalTrueFriend);
            return stmt;
        });
    }

        @Override
        public void deleteFriend (User user, User friend){
            String sqlQuery = "delete from FRIENDSHIP where USER1_ID = ? AND USER2_ID = ?";
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                stmt.setInt(1, user.getId());
                stmt.setInt(2, friend.getId());
                return stmt;
            });

        }

        @Override
        public List getFriends (int userId) throws SQLException {
            final String sqlQuery = "select USER2_ID from USERS where USER1_ID = ?";
            List<Integer> userFriends = jdbcTemplate.queryForList(sqlQuery, Integer.class, userId);
            return userFriends;
        }


        @Override
        public List getCommonFriends ( int userId, int otherId){
            return null;
        }




}
