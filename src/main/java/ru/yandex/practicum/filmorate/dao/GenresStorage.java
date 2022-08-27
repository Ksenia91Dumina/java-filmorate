package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenresStorage {
    boolean setGenresForFilm(int filmId, Collection<Genre> genres);

    List<Genre> getGenresByFilmId(int filmId);

    void deleteGenresForFilm(int filmId);

    Genre getGenreById(int id);

    List<Genre> getGenres();
}