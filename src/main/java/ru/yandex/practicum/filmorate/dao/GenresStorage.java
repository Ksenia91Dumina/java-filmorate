package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenresStorage {
    void setGenresForFilm(int filmId);
    List<Genre> getGenresByFilmId(int filmId);

    void deleteGenresForFilm(int filmId);

    Genre getGenreById(int id);

    List<Genre> getGenres();
}