package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genresStorage;
    private final UserDbStorage userStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film get(int filmId) {
        try {
            return filmStorage.get(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм c id" + filmId + " не найден");
        }
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public void removeFilm(int filmId) {
        filmStorage.removeFilm(filmId);
    }

    public Film updateFilm(Film film) {
        try {
            filmStorage.get(film.getId());
            return filmStorage.updateFilm(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id " + film.getId() + "не найден");
        }
    }

    public void addLike(int userId, int filmId) {
        try {
            filmStorage.get(filmId);
            userStorage.getUserById(userId);
            filmStorage.addLike(userId, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id " + filmId + "не найден");
        }
    }

    public void deleteLike(int userId, int filmId) throws SQLException {
        try {
            filmStorage.get(filmId);
            userStorage.getUserById(userId);
            filmStorage.deleteLike(userId, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id " + filmId + "не найден");
        }
    }

    public List<Film> raitingFilm(int count) {
        return filmStorage.raitingFilm(count);
    }

}