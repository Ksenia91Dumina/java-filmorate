package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.dao.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
public class FilmValidations {

    public static LocalDate earliestDate = LocalDate.of(1895, 12, 28);
    public static FilmController fc = new FilmController(new FilmService());

    public static void validateDescription(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно содержать не более 200 символов");
        }
    }

    public static void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(earliestDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }

    public static void validateForUpdateFilm(Film film) {
        Collection<Film> films = fc.getAllFilms();

        if (!(films.isEmpty())) {
            if (!films.contains(film)) {
                log.info("Фильма не существует");
                throw new ValidationException("Фильма не существует");
            }
        }
    }

}
