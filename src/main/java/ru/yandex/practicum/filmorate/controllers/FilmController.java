package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private int uniqueID = 0;
    public static LocalDate earliestDate = LocalDate.of(1895, 12, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("films")
    public Map<Integer, Film> getAllFilms() {
        //  Здесь если вернуть копию мапы - подойдет?
        return Map.copyOf(films);
    }

    @PostMapping("films")
    public Film createFilm(@RequestBody Film film) {
        film.setFilmId(getUniqueID());
        FilmValidations.validateDescription(film);
        FilmValidations.validateDate(film, earliestDate);
        films.put(film.getFilmId(), film);
        log.info("Создан фильм " + film.getFilmName());
        return film;
    }

    @PutMapping("films")
    public Film updateFilm(@RequestBody Film film) {
        if (!(films.isEmpty())) {
            if (films.containsKey(film.getFilmId())) {
                films.put(film.getFilmId(), film);
                log.info("Изменен фильм " + film.getFilmName());
            } else {
                log.info("Фильма не существует");
                return null;
            }
        }
        return film;
    }


    public int getUniqueID() {
        return ++uniqueID;
    }
}
