package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationsTest {

    @Test
    void validateDescriptionTest() throws IOException {
        Genre genre1 = Genre.builder()
                .id(2)
                .name("Драма")
                .build();
        Genre genre2 = Genre.builder()
                .id(4)
                .name("Триллер")
                .build();
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genre1);
        genres.add(genre2);

        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 12))
                .duration(120)
                .mpa(mpa)
                .genres(genres)
                .build();

        String text = null;
        text = new String(Files.readAllBytes(Paths.get(
                "src/test/java/ru/yandex/practicum/filmorate/controllers/textMoreThan200.txt")),
                StandardCharsets.UTF_8);

        film.setDescription(text);
        assertThrows(ValidationException.class, () -> FilmValidations.validateDescription(film));
    }

    @Test
    void validateNameTest() {
        Genre genre1 = Genre.builder()
                .id(2)
                .name("Драма")
                .build();
        Genre genre2 = Genre.builder()
                .id(4)
                .name("Триллер")
                .build();
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genre1);
        genres.add(genre2);

        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 12))
                .duration(120)
                .mpa(mpa)
                .genres(genres)
                .build();

        film.setName("");
        assertThrows(ValidationException.class, () -> FilmValidations.validateName(film));
    }

    @Test
    void validateDateTest() {
        Genre genre1 = Genre.builder()
                .id(2)
                .name("Драма")
                .build();
        Genre genre2 = Genre.builder()
                .id(4)
                .name("Триллер")
                .build();
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genre1);
        genres.add(genre2);

        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        Film film = Film.builder()
                .id(1)
                .name("Name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 12))
                .duration(120)
                .mpa(mpa)
                .genres(genres)
                .build();

        film.setReleaseDate(LocalDate.of(1700, 10, 10));
        assertThrows(ValidationException.class, () -> FilmValidations.validateDate(film));
    }

    @Test
    void validateDuration() {
        Genre genre1 = Genre.builder()
                .id(2)
                .name("Драма")
                .build();
        Genre genre2 = Genre.builder()
                .id(4)
                .name("Триллер")
                .build();
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genre1);
        genres.add(genre2);

        Mpa mpa = Mpa.builder()
                .id(1)
                .name("G")
                .build();

        Film newFilm = Film.builder()
                .id(1)
                .name("Name")
                .description("description")
                .releaseDate(LocalDate.of(1995, 12, 12))
                .duration(120)
                .mpa(mpa)
                .genres(genres)
                .build();

        newFilm.setDuration(-50);
        assertThrows(ValidationException.class, () -> FilmValidations.validateDuration(newFilm));
    }

}