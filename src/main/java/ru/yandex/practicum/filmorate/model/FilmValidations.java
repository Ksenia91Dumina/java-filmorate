package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;


import java.time.LocalDate;


@Slf4j
public class FilmValidations {

    public static LocalDate earliestDate = LocalDate.of(1895, 12, 28);

    public static void validateDescription(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно содержать не более 200 символов");
        }
    }

    public static void validateName(Film film) {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
    }

    public static void validateDate(Film film) {
        if (film.getReleaseDate().isBefore(earliestDate)) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
        }
    }


    public static void validateDuration(Film film) {
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной или = 0");
        }
    }

}