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

    void removeFilm(int filmId);

    Film updateFilm(Film film);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List raitingFilm(int count);
}