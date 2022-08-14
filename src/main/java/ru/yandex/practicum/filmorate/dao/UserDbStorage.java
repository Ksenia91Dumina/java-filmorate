package ru.yandex.practicum.filmorate.dao;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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


    @Override
    public User getUserById(ResultSet rs, int userId) throws SQLException {
        final String sqlQuery = "select * from USERS where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
        if (users.size() != 1) {
            return null;
        }
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sqlQuery);
        final Object value = maps.get(0).values().iterator().next();
        Integer value2 = jdbcTemplate.queryForObject(sqlQuery, Integer.class);
        jdbcTemplate.queryForList(sqlQuery);
        rs.getInt("USER_ID");
        return users.get(0);
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
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
        public HashMap getFriends (int userId) throws SQLException {
            final String sqlQuery = "select USER2_ID from USERS where USER1_ID = ?";
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                stmt.setInt(1, userId);
                return stmt;
            });
            final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
            if (users.size() != 1) {
                return null;
            }
            final List<Integer> friendsId = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper(Integer.class));
            HashMap<Integer, User> friends = new HashMap<>();
            List<User> userMap = getUserMap();
            for(Integer id : friendsId) {
                for (User user : userMap) {
                    if(user.getId() == id)
                    friends.put(id, user);
                }
            }
            return friends;


            //совсем не моуг понять какой ход действий тут должен быть с поиском друзей((

        }

        @Override
        public List getCommonFriends ( int userId, int otherId){
            return null;
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


}
