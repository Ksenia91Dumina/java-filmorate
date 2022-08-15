package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmService {

    UserDbStorage userStorage;

    FilmDbStorage filmStorage = new FilmDbStorage();

    public List<Film> getAllFilms() {
        return filmStorage.getFilmMap();
    }

    public Film get(int filmId) throws SQLException {
        final Film film;
        try {
            film = filmStorage.get(filmId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmStorage.get(filmId);
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public void removeFilm(Film film) {
        filmStorage.removeFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(int userId, int filmId) throws SQLException {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.get(filmId);
        if (userStorage.getUserMap().contains(userId) && filmStorage.getFilmMap().contains(filmId)) {
            filmStorage.addLike(user, film);
        }
    }

    public void deleteLike(int userId, int filmId) throws SQLException {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.get(filmId);
        filmStorage.deleteLike(user, film);
    }

    public void raitingFilm(int count) {
        if (count == 0)
            count = 10;
        filmStorage.raitingFilm(count);
    }

    public void setGenres(Film film){
        filmStorage.setFilmGenre(film);
    }
}