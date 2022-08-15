package ru.yandex.practicum.filmorate.dao;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;


@NoArgsConstructor
@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilmMap() {
        String sqlQuery = "select * from FILMS";
        List films = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper(Film.class));
        return films;
    }

    public Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new MPA(rs.getInt("RAITING_MPA.MPA_ID"), rs.getString("RAITING_MPA.NAME")),
                new HashSet<>(rs.getInt("GENRES.GENRE_ID"))
        );
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate release = film.getReleaseDate();
            if (release == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(release));
            }
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        return film;
    }


    @Override
    public boolean removeFilm(Film film) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, film.getId()) > 0;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILMS set " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?" +
                "where USER_ID = ?";
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
    public Film get(int filmId) throws SQLException {
        final String sqlQuery = "select * from FILMS " +
                "join RAITING_MPA ON FILMS.MPA_ID = RAITING_MPA.MPA_ID " +
                "where FILM_ID = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, filmId);
        return film;
    }

    public List loadFilmGenre(Film film) {
        final String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = film.getId() ";
        final List<Integer> filmGenres = jdbcTemplate.queryForList(sqlQuery, Integer.class, film.getId());
        return filmGenres;
    }

    public void setFilmGenre(Film film) {
        int filmId = film.getId();
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        jdbcTemplate.update("delete from FILM_GENRE where FILM_ID = ?", filmId);
        for (Genre genre : film.getGenres()) {
            String sqlQuery = "insert into FILM_GENRE (GENRE_ID, FILM_ID) values (?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, genre.getId());
                preparedStatement.setInt(2, filmId);
                return preparedStatement;
            });
        }
    }

    @Override
    public void addLike(User user, Film film) {

    }

    @Override
    public void deleteLike(User user, Film film) {

    }

    @Override
    public List raitingFilm(int count) {
        return null;
    }


}
