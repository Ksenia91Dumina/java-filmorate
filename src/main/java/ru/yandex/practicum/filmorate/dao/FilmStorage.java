package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Collection<Integer> getFilmMap();

    Film get(int filmId);

    Film save(Film film);

    void addLike(User user, Film film);

    void deleteLike(User user, Film film);

    Set raitingFilm(int count);
}
