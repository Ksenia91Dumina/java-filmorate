package ru.yandex.practicum.filmorate.dao;

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
import java.util.stream.Collectors;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Collection<Film> getFilmMap() {
        String sqlQuery = "select * from FILMS";
        Collection films = jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper(Film.class));
        return films;
    }

    @Override
    public Film get(int filmId) {
        final String sqlQuery = "select * from FILMS JOIN RAITING_MPA ON () where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, filmId);
        if (films.size() != 1) {
            return null;
        }
        final List<Map<String, Object>> maps = jdbcTemplate.queryForList(sqlQuery);
        final Object value = maps.get(0).values().iterator().next();
        Integer value2 = jdbcTemplate.queryForObject(sqlQuery, Integer.class);
        jdbcTemplate.queryForList(sqlQuery);
        ResultSet rs = null;
        rs.getInt("FILM_ID");
        return films.get(0);
    }

    void setFilmGenre(Film film){
        // SQL DELETE FROM FILM_GENRES WHERE FILM_ID = film.getId()
        if(film.getGenres() == null || film.getGenres().isEmpty()){
            return;
        }
        for (Genre genre : film.getGenres()){
            //SQL INSERT INTO FILM_GENRES(FILM_ID, GENRE_ID) VALUES(?,?)
        }
    }

    void loadFilmGenre(Film film){
        final String sqlQuery = "SELECT NAME FROM FILM_GENRE WHERE FILM_ID = film.getId() "
        final List<Map<String, Object>> filmGenres = jdbcTemplate.queryForList(sqlQuery);
    }


    // с жанрами тут тоже ступор...
    // Владимир на вебинаре рассказывал про два метода для нахождения жанров, но я не могу понять как реализовать
    void loadFilmGenreSlow(List<Film> films){
        for(Film film: films){
            loadFilmGenre(film);
        }
    }

    void loadAllFilmGenre(List<Film> films){
        final List<Integer> ids = films.stream().map(f -> f.getId()).collect(Collectors.toList());
        // SELECT FILM_ID, GENRES.* FROM GENRES WHERE FILM_ID = film.getId()
        final Map<Integer, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film, (a, b) -> b));
        filmMap.get(FILM_ID).addGenre(new Genre());
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

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
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
                ,film.getDuration()
                , film.getMpa()
                , film.getId());
        return film;
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
