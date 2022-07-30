package ru.yandex.practicum.filmorate.controllers;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("films")
@Slf4j
public class FilmController {
    @Autowired
    private final FilmService filmService = new FilmService();
    private int uniqueID = 0;

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return filmService.get(filmId);
    }

    @PostMapping()
    public Film createFilm(@RequestBody Film film) {
        film.setId(++uniqueID);
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        filmService.save(film);
        log.info("Создан фильм " + film.getName());
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        FilmValidations.validateForUpdateFilm(film);
        filmService.updateFilm(film);
        log.info("Изменен фильм " + film.getName());
        return film;
    }

    public void removeFilm(Film film){
        filmService.removeFilm(film);
    }

    @PutMapping("films/{id}/like/{userId}")
    public void addLike(@RequestBody int id, @RequestBody int userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void deleteLike(@RequestBody int id, @RequestBody int userId) {
        filmService.deleteLike(userId, id);
    }

    @GetMapping("films/popular?count={count}")
    public void raitingFilm(@RequestBody int count) {
        filmService.raitingFilm(count);
    }

}
