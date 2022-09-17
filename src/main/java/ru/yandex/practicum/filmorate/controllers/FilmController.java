package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Slf4j
public class FilmController {
    public final FilmService filmService;

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Запрос GET - вывод всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        log.info("Запрос GET - вывод фильма по номеру id");
        return filmService.get(filmId);
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Запрос POST - создание фильма");
        FilmValidations.validateName(film);
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        FilmValidations.validateDuration(film);
        filmService.save(film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос PUT - обновление фильма");
        FilmValidations.validateName(film);
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        FilmValidations.validateDuration(film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(int filmId) {
        filmService.removeFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable  int filmId, @PathVariable  int userId) throws SQLException {
        log.info("Запрос PUT - добавление лайка");
        return filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int filmId, @PathVariable int userId) throws SQLException {
        log.info("Запрос DELETE - удаление лайка");
        filmService.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> raitingFilm(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос GET - вывод популярных фильмов");
        return filmService.raitingFilm(count);
    }

}