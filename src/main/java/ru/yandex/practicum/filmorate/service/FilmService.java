package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenresStorage;
import ru.yandex.practicum.filmorate.dao.Impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final GenresStorage genresStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, MpaDbStorage mpaStorage,
                       GenresStorage genresStorage, UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genresStorage = genresStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getFilmMap();
        for (Film film : films) {
            film.setGenres(genresStorage.getGenresByFilmId(film.getId()));
        }
        return films;
    }

    public Film get(int filmId) {
        Film film;
        try {
            film = filmStorage.get(filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм не найден");
        }
        int mpaId = film.getMpa().getId();
        Mpa mpa = mpaStorage.getMpaById(mpaId);
        List<Genre> genres = genresStorage.getGenresByFilmId(filmId);
        film.setMpa(mpa);
        film.setGenres(genres);
        return film;
    }

    public Film save(Film film) {
        Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
        film.setMpa(mpa);
        film = filmStorage.save(film);
        return film;

    }

    public void removeFilm(int filmId) {
        filmStorage.removeFilm(filmId);
    }

    public Film updateFilm(Film film) {
        if (!(getAllFilms().isEmpty())) {
            if (!getAllFilms().contains(film)) {
                log.info("Фильма не существует");
                throw new NotFoundException("Фильм с id " + film.getId() + "не найден");
            }
            filmStorage.updateFilm(film);
            genresStorage.deleteGenresForFilm(film.getId());
            genresStorage.setGenresForFilm(film.getId());
            filmStorage.updateFilm(film);
        }
        return film;
    }

    public void addLike(int userId, int filmId) throws SQLException {
        filmStorage.addLike(userId, filmId);
    }

    public void deleteLike(int userId, int filmId) throws SQLException {
        filmStorage.deleteLike(userId, filmId);
    }

    public List<Film> raitingFilm(int count) {
        List<Film> films = filmStorage.raitingFilm(count);
        for (Film film : films) {
            film.setGenres(genresStorage.getGenresByFilmId(film.getId()));
        }
        return films;
    }

}