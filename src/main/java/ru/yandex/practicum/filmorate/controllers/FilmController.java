package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return filmService.get(filmId);
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {

        FilmValidations.validateName(film);
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        FilmValidations.validateDuration(film);
        filmService.save(film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(int filmId) {
        filmService.removeFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@RequestBody int filmId, @RequestBody int userId) throws SQLException {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@RequestBody int filmId, @RequestBody int userId) throws SQLException {
        filmService.deleteLike(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> raitingFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.raitingFilm(count);
    }

}