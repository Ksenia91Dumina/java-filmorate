package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Slf4j
public class FilmController {

    int uniqueID = 0;
    LocalDate earliestDate = LocalDate.of(1895,12,28);
    private final HashMap<Integer, Film> films = new HashMap<>();

    //получение всех фильмов
    @GetMapping("films")
    public HashMap<Integer, Film> getAllFilms() {
        return films;
    }

    //добавление фильма
    @PostMapping("films")
    public Film createFilm(@RequestBody Film film) {
        film.setFilmId(getUniqueID());
        validateDescription(film);
        validateDate(film);
        films.put(film.getFilmId(), film);
        log.info("Создан фильм " + film.getFilmName());
        return film;
    }

    //изменение фильма
    @PutMapping("films")
    public Film updateFilm(@RequestBody Film film){
        if (!(films.isEmpty())) {
            if (films.containsKey(film.getFilmId())) {
                films.put(film.getFilmId(), film);
                log.info("Изменен фильм " + film.getFilmName());
            } else{
                log.info("Фильма не существует");
                return null;
            }
        } return film;
    }

    //Валидация описания фильма
    void validateDescription(Film film){
        if(film.getFilmDescription().length() > 200){
            throw new RuntimeException("Описание фильма должно содержать не более 200 символов");
        }
    }

    //валидация даты релиза
    void validateDate(Film film){
        if(film.getReleaseDate().isBefore(earliestDate)){
            throw new RuntimeException("Дата релиза не может быть раньше 28.12.1895");
        }
    }

    public int getUniqueID() {
        return ++uniqueID;
    }
}
