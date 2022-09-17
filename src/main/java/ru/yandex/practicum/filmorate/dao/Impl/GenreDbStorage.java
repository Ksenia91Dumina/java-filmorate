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
    private static JdbcTemplate jdbcTemplate;
    private static FilmDbStorage filmDbStorage;

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM GENRE";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre);
        return genres;
    }

    @Override
    public Genre getGenreById(int genre_id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, genre_id);
    }

    /*@Override
    public void setGenresForFilm(int filmId) {
        String sqlQuery = "INSERT INTO FILM_GENRE(film_id, genre_id)  VALUES (?,?)";
        Film film = filmDbStorage.get(filmId);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }
    }*/

    /*@Override
    public void deleteGenresForFilm(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        try {
            int updatedRows = jdbcTemplate.update(sqlQuery, filmId);
            if (updatedRows > 0) {
                log.info("Жанры удалены");
            }
        } catch (EmptyResultDataAccessException e) {
            log.info("Невозможно удалить жанры - список пуст");
            throw new NotFoundException("Для фильма с id = " + filmId + " cписок жанров не найден");
        }
    }*/

    @Override
    public Collection<Genre> getGenresByFilmId(int filmId) {
        String sql = "SELECT * FROM GENRES g " +
                "JOIN FILM_GENRE fg on g.GENRE_ID = fg.GENRE_ID " +
                "WHERE fg.FILM_ID = ? ";
        return jdbcTemplate.query(sql, this::makeGenre, filmId);
    }

}
