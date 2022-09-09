package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Primary
@Getter
@Setter
@Slf4j
public class GenreDbStorage implements GenresStorage {
    private JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
    }

    @Override
    public void setGenresForFilm(int filmId) {
        String sqlQuery = "INSERT INTO FILM_GENRE(film_id, genre_id)  VALUES (?,?)";
        Film film = filmDbStorage.get(filmId);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }
    }


    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlQuery = "SELECT DISTINCT g.GENRE_ID, g.NAME FROM GENRE AS g" +
                " INNER JOIN FILM_GENRE AS fg ON g.GENRE_ID = fg.GENRE_ID" +
                " WHERE FILM_ID = ? ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, this::makeGenre, filmId);
    }

    @Override
    public void deleteGenresForFilm(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        int updatedRows = jdbcTemplate.update(sqlQuery, filmId);
        if (updatedRows > 0) {
            log.info("Жанры удалены");
        } else {
            log.info("Невозможно удалить жанры - список пуст");
        }
    }

    @Override
    public Genre getGenreById(int genre_id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, genre_id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр не найден");
        }
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM GENRE ORDER BY GENRE_ID";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre);
        return genres;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("genre_id"),
                rs.getString("name"));
    }
}
