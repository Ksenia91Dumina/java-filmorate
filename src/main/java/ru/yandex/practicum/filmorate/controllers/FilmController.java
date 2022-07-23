package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("films")
@Slf4j
public class FilmController {
    @Autowired
    FilmService filmService;
    private int uniqueID = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @GetMapping("/{filmId}")
    Film getFilm(@PathVariable int filmId){
        return filmService.get(filmId);
    }

    @PostMapping()
    public Film createFilm(@RequestBody Film film) {
        film.setId(++uniqueID);
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        films.put(film.getId(), film);
        log.info("Создан фильм " + film.getName());
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (!(films.isEmpty())) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Изменен фильм " + film.getName());
            } else {
                log.info("Фильма не существует");
                throw new ValidationException("Фильма не существует");
            }
        }
        return film;
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
    public void raitingFilm(@RequestBody int count){
        filmService.raitingFilm(count);
    }
}
