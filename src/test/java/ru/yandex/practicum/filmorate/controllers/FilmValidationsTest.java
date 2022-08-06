package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

    public FilmController filmController = new FilmController(new FilmService());


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
    void validateDateTest() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1700, 10, 10));
        assertThrows(ValidationException.class, () -> FilmValidations.validateDate(film));
    }

    @Test
    void validateForUpdateFilmTest() {
        Film newFilm = new Film();
        newFilm.setId(1);
        newFilm.setName("New Film name");
        newFilm.setDescription("description of New Film");
        newFilm.setReleaseDate(LocalDate.of(2022, 06, 15));
        newFilm.setDuration(170);
        filmController.createFilm(newFilm);
        filmController.removeFilm(newFilm);
        assertThrows(NotFoundException.class, () -> FilmValidations.validateForUpdateFilm(newFilm));
    }
}