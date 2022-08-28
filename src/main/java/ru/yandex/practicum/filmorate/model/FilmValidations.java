package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.dao.Impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Objects;


@Slf4j
public class FilmValidations {

    public static LocalDate earliestDate = LocalDate.of(1895, 12, 28);
    public static FilmController fc = new FilmController(new FilmService(new FilmDbStorage(new JdbcTemplate()),
            new MpaDbStorage(new JdbcTemplate()),
            new GenreDbStorage(new JdbcTemplate(), new FilmDbStorage(new JdbcTemplate())),
            new UserDbStorage()));

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

    //я не знаю что здесь делать((( как правильно проверить(((
    public static void validateForUpdateFilm(Film film) {
        /*List<Film> films = fc.getAllFilms();
        if (!films.isEmpty()) {
            if (!films.contains(film)) {
                log.info("Фильма не существует");
                throw new ValidationException("Фильма не существует");
            }
        }*/
        /*if(film == undefined){
            throw new ValidationException("Фильма не существует");
        }*/

        if(film != Objects.requireNonNull(film)){
            throw new ValidationException("Фильма не существует");
        }

    }

    public static void validateDuration(Film film){
        if(film.getDuration() <= 0){
            throw new ValidationException("Продолжительность фильма не может быть отрицательной или = 0");
        }
    }

}