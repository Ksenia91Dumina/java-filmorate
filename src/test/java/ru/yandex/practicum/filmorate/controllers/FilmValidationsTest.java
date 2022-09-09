package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.Impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.Impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationsTest {

    @Test
    void validateDescriptionTest() throws IOException {
        Film film = new Film();
        String text = null;
        text = new String(Files.readAllBytes(Paths.get(
                "src/test/java/ru/yandex/practicum/filmorate/controllers/textMoreThan200.txt")),
                StandardCharsets.UTF_8);

        film.setDescription(text);
        assertThrows(ValidationException.class, () -> FilmValidations.validateDescription(film));
    }

    @Test
    void validateNameTest() {
        Film film = new Film();
        film.setName("");
        assertThrows(ValidationException.class, () -> FilmValidations.validateName(film));
    }

    @Test
    void validateDateTest() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1700, 10, 10));
        assertThrows(ValidationException.class, () -> FilmValidations.validateDate(film));
    }

    @Test
    void validateDuration() {
        Film newFilm = new Film();
        newFilm.setDuration(-50);
        assertThrows(ValidationException.class, () -> FilmValidations.validateDuration(newFilm));
    }

}