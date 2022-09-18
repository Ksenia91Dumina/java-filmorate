package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor

public class GenreDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM GENRES";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre);
        return genres;
    }

    @Override
    public Genre getGenreById(int genre_id) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, genre_id);
    }

    @Override
    public Collection<Genre> getGenresByFilmId(int filmId) {
        String sql = "SELECT * FROM GENRES g " +
                "JOIN FILM_GENRES fg on g.GENRE_ID = fg.GENRE_ID " +
                "WHERE fg.FILM_ID = ? ";
        return jdbcTemplate.query(sql, this::makeGenre, filmId);
    }

}
