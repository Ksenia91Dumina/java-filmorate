package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("films")

@Slf4j
public class FilmController {
    public final FilmService filmService;
    private int uniqueID = 1;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) throws SQLException {
        return filmService.get(filmId);
    }

    @PostMapping()
    public Film createFilm(@RequestBody Film film) {
        film.setId(++uniqueID);
        FilmValidations.validateName(film);
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        FilmValidations.validateDuration(film);
        filmService.save(film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        filmService.updateFilm(film);
        return film;
    }

    @DeleteMapping("/{id}")
    public void removeFilm(int filmId) {
        filmService.removeFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@RequestBody int id, @RequestBody int userId) throws SQLException {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@RequestBody int id, @RequestBody int userId) throws SQLException {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> raitingFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.raitingFilm(count);
    }

}