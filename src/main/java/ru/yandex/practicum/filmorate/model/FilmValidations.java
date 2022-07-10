package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.controllers.FilmController;

import java.time.LocalDate;

public class FilmValidations {

    public static void validateDescription(Film film) {
        if (film.getFilmDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно содержать не более 200 символов");
        }
    }

    public static void validateDate(Film film, LocalDate date) {
        if (film.getReleaseDate().isBefore(FilmController.earliestDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }
}
