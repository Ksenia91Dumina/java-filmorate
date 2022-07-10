package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmValidations;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationsTest {

    protected FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();
    }

    @Test
    void validateDescription() throws IOException {
        Film film = new Film();
        String text = null;
        text = new String(Files.readAllBytes(Paths.get(
                "src/test/java/ru/yandex/practicum/filmorate/controllers/textMoreThan200.txt")),
                StandardCharsets.UTF_8);

        film.setFilmDescription(text);
        assertThrows(ValidationException.class, () -> FilmValidations.validateDescription(film));
    }

    @Test
    void validateDate() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1700, 10, 10));
        assertThrows(ValidationException.class, () -> FilmValidations.validateDate(film, film.getReleaseDate()));
    }
}