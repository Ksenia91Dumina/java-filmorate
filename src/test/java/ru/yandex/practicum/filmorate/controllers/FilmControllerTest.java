package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void init(){
        filmController = new FilmController();
    }

    @Test
    void validateDescription() {
        Film film = new Film();
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get(
                    "src/test/java/ru/yandex/practicum/filmorate/controllers/textMoreThan200.txt")),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        film.setFilmDescription(text);
        assertThrows(RuntimeException.class, () -> filmController.validateDescription(film));
    }

    @Test
    void validateDate() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.of(1700,10,10));
        assertThrows(RuntimeException.class, () -> filmController.validateDate(film));
    }
}