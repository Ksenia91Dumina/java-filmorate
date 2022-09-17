package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenresStorage {

    Genre getGenreById(int id);
    List<Genre> getGenres();

    Collection<Genre> getGenresByFilmId(int filmId);
}