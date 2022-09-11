package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.LikesException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Repository
@Primary
@Getter
@Setter
@Slf4j

public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilmMap() {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                "f.MPA_ID, m.NAME AS MPA_NAME " +
                "FROM FILMS f " +
                "JOIN MPA m on f.MPA_ID = m.MPA_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return films;
    }

    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"),
                        rs.getString("mpa_name")));
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate release = film.getReleaseDate();
            if (release == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(release));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey().intValue()));
        log.info("Сохранен фильм с id = " + film.getId());
        return film;
    }


    @Override
    public void removeFilm(int filmId) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        int result = jdbcTemplate.update(sqlQuery, filmId);
        if (result > 0) {
            log.info("Удален фильм с id = " + filmId);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
         jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa()
                , film.getId());
        return film;
    }

    @Override
    public Film get(int filmId) {
        Film film = new Film();
        final String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                "f.MPA_ID, MPA.NAME AS MPA_NAME " +
                "FROM FILMS f " +
                "LEFT JOIN MPA ON MPA.MPA_ID = f.MPA_ID " +
                "WHERE FILM_ID = ?";
        try {
            List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, filmId);
            film = films.get(0);
            return film;
           // return jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public void addLike(int userId, int filmId) {
        String sqlQuery = "INSERT INTO USERS_LIKES(USER_ID, FILM_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, userId, filmId);
            log.info("Добавлен лайк от пользователя с id = ", userId);
        } catch (DataAccessException e) {
            log.info("Невозможно добавить лайк для фильма с id = ", userId);
            throw new LikesException("Невозможно добавить лайк");
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sqlQuery = "DELETE FROM USERS_LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        int rowsDeleted = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (rowsDeleted > 0) {
            log.info("Удален лайк от пользователя с id = ", userId);
        } else {
            throw new LikesException("Невозможно удалить лайк - не найден");
        }
    }

    @Override
    public List<Film> raitingFilm(int count) {
        String sql = "SELECT * FROM FILMS " +
                "LEFT JOIN USERS_LIKES ON FILMS.FILM_ID = USERS_LIKES.FILM_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY COUNT(USERS_LIKES.FILM_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, this::makeFilm, count);
    }


}
