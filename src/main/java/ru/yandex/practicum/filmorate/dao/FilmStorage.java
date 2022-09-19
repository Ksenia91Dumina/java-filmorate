package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film get(int filmId) throws SQLException;

    Film save(Film film);

    void removeFilm(int filmId);

    Film updateFilm(Film film);

    Film addLike(int userId, int filmId);

    Film deleteLike(int userId, int filmId);

    List<Film> raitingFilm(int count);
}