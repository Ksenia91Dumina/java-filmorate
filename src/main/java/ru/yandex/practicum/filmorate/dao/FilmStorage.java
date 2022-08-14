package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getFilmMap();

    Film get(int filmId) throws SQLException;

    Film save(Film film);

    boolean removeFilm(Film film);

    Film updateFilm(Film film);

    void addLike(User user, Film film);

    void deleteLike(User user, Film film);

    List raitingFilm(int count);
}