package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Component("FilmDbStorage")
@Primary
@RequiredArgsConstructor

public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    private Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaService.getMpaById(resultSet.getInt("rating_id")))
                .genres(new LinkedHashSet<>(genreService.getGenresByFilmId(resultSet.getInt("film_id"))))
                .build();
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM FILMS";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return films;
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        String setGenreForFilm = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID)  VALUES (?,?)";
        if (film.getGenres() != null) {
            LinkedHashSet<Genre> genres = new LinkedHashSet<>();
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(setGenreForFilm, film.getId(), genre.getId());
                genres.add(genreService.getGenreById(genre.getId()));
            }
            film.setGenres(genres);
            log.info("Сохранен фильм с id = " + film.getId());
        }
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
                , film.getMpa().getId()
                , film.getId());
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        String deleteGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ? ";
        String setGenreForFilm = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) " +
                "VALUES(?, ?)";
        if (film.getGenres() != null) {
            jdbcTemplate.update(deleteGenres, film.getId());
            LinkedHashSet<Genre> genres = new LinkedHashSet<>();
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(setGenreForFilm, film.getId(), genre.getId());
                genres.add(genreService.getGenreById(genre.getId()));
            }
            film.setGenres(genres);
        }
        return film;
    }

    @Override
    public Film get(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS " +
                "WHERE FILMS.FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, filmId);
    }

    @Override
    public Film addLike(int userId, int filmId) {
        String sqlQuery = "INSERT INTO USERS_LIKES(USER_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        return get(filmId);
    }

    @Override
    public Film deleteLike(int userId, int filmId) {
        String sqlQuery = "DELETE FROM USERS_LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        return get(filmId);
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

