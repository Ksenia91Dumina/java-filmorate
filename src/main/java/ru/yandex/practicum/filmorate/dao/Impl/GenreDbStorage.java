package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class GenreDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean setGenresForFilm(int filmId, Collection<Genre> genres) {
        String sqlQuery = "INSERT INTO FILM_GENRE(film_id, genre_id) VALUES (?, ?)";
        genres.forEach(genre -> jdbcTemplate.update(sqlQuery, filmId, genre.getId()));
        return true;
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
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }
}
