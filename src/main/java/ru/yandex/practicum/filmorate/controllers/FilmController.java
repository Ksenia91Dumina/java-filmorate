package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private int uniqueID = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("films")
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping("films")
    public Film createFilm(@RequestBody Film film) {
        film.setFilmId(getUniqueID());
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film);
        films.put(film.getFilmId(), film);
        log.info("Создан фильм " + film.getName());
        return film;
    }

    @PutMapping("films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (!(films.isEmpty())) {
            if (films.containsKey(film.getFilmId())) {
                films.put(film.getFilmId(), film);
                log.info("Изменен фильм " + film.getName());
            } else {
                log.info("Фильма не существует");
                throw new ValidationException("Фильма не существует");
            }
        }
        return film;
    }


    public int getUniqueID() {
        return ++uniqueID;
    }
}
