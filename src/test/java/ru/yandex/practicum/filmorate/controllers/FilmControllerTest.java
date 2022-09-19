package ru.yandex.practicum.filmorate.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmControllerTest {

    private final FilmController filmController;
    private final JdbcTemplate jdbcTemplate;

    private Film film1;
    private Film film2;

    @Autowired
    public FilmControllerTest(FilmController filmController, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmController = filmController;
    }

    @BeforeEach
    void init() {
        String sqlQuery = "DELETE FROM FILMS ";
        jdbcTemplate.update(sqlQuery);

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

        Mpa mpa1 = Mpa.builder()
                .id(1)
                .name("G")
                .build();
        Mpa mpa2 = Mpa.builder()
                .id(2)
                .name("PG")
                .build();

        film1 = Film.builder()
                .id(1)
                .name("First film")
                .description("First film description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(180)
                .mpa(mpa1)
                .genres(genres)
                .build();
        film2 = Film.builder()
                .id(2)
                .name("Second film")
                .description("Second film description")
                .releaseDate(LocalDate.of(2020, 2, 2))
                .duration(180)
                .mpa(mpa2)
                .genres(genres)
                .build();

        filmController.createFilm(film1);
        filmController.createFilm(film2);
    }


    @Test
    void getAllFilms() {
        List<Film> films = filmController.getAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void createFilm() {
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
                .id(3)
                .name("New film")
                .description("New film description")
                .releaseDate(LocalDate.of(2022, 8, 27))
                .duration(210)
                .mpa(mpa)
                .genres(genres)
                .build();
        filmController.createFilm(newFilm);
        List<Film> films = filmController.getAllFilms();
        assertEquals(3, films.size());
    }

    @Test
    void updateFilm() {
        Genre genre1 = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
        Genre genre2 = Genre.builder()
                .id(6)
                .name("Боевик")
                .build();
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genre1);
        genres.add(genre2);

        Mpa mpa1 = Mpa.builder()
                .id(2)
                .name("PG")
                .build();
        Mpa mpa2 = Mpa.builder()
                .id(3)
                .name("PG-13")
                .build();

        Film newFilm = Film.builder()
                .id(4)
                .name("New film")
                .description("New film description")
                .releaseDate(LocalDate.of(2022, 8, 27))
                .duration(210)
                .mpa(mpa1)
                .genres(genres)
                .build();
        filmController.createFilm(newFilm);
        newFilm.setName("New name for film");
        newFilm.setMpa(mpa2);
        filmController.updateFilm(newFilm);
        assertEquals("New name for film", newFilm.getName());
        assertEquals(mpa2, newFilm.getMpa());
    }

    @Test
    void deleteFilmTest() {
        filmController.removeFilm(film1.getId());
        List<Film> films = filmController.getAllFilms();
        assertEquals(1, films.size());
    }

}