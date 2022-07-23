package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

public class FilmValidations {

    public static LocalDate earliestDate = LocalDate.of(1895, 12, 28);

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
}
