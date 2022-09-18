package ru.yandex.practicum.filmorate.dao.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorageTest(FilmDbStorage filmStorage, GenreDbStorage genreStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.jdbcTemplate = jdbcTemplate;
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

        Film film1 = Film.builder()
                .id(1)
                .name("First film")
                .description("First film description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(180)
                .mpa(mpa1)
                .genres(genres)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("Second film")
                .description("Second film description")
                .releaseDate(LocalDate.of(2020, 2, 2))
                .duration(180)
                .mpa(mpa2)
                .genres(genres)
                .build();

        filmStorage.save(film1);
        filmStorage.save(film2);
    }


    @Test
    void getFilmMapTest() {
        List<Film> films = filmStorage.getAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void saveTest() {
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
        filmStorage.save(newFilm);
        List<Film> films = filmStorage.getAllFilms();
        assertEquals(3, films.size());
    }

    @Test
    void updateTest(){
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
        filmStorage.save(newFilm);
        newFilm.setName("New name for film");
        newFilm.setMpa(mpa2);
        filmStorage.updateFilm(newFilm);
        assertEquals("New name for film", newFilm.getName());
        assertEquals(mpa2, newFilm.getMpa());
    }

}
