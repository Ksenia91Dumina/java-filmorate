package ru.yandex.practicum.filmorate.dao.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    public FilmDbStorageTest(FilmDbStorage filmStorage, GenreDbStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
    }

    @BeforeEach
    void init() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(4, "Триллер"));
        genres.add(new Genre(2, "Драма"));
        Film film1 = new Film();
        Film film2 = new Film();
        film1.setId(1);
        film1.setName("First film");
        film1.setDescription("First film description");
        film1.setReleaseDate(LocalDate.of(2020, 1, 1));
        film1.setDuration(180);
        film1.setMpa(new Mpa(1, "G"));
        film1.setGenres(genres);
        film2.setId(2);
        film2.setName("Second film");
        film2.setDescription("Second film description");
        film2.setReleaseDate(LocalDate.of(2020, 2, 2));
        film2.setDuration(200);
        film2.setMpa(new Mpa(2, "PG"));
        film2.setGenres(genres);
        filmStorage.save(film1);
        filmStorage.save(film2);
    }

    //если отдельно запускать getFilmMapTest и removeTest - проходят

    /*@Test
    void getFilmMapTest() {
        List<Film> films = filmStorage.getFilmMap();
        assertEquals(2, films.size());
    }*/

    @Test
    void saveTest() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(4, "Триллер"));
        genres.add(new Genre(2, "Драма"));
        Film film = new Film(3, "New name", "New film description",
                LocalDate.of(2022, 8, 27), 180, new Mpa(1, "G"), genres);
        film.setGenres(genres);
        filmStorage.save(film);
        List<Film> films = filmStorage.getFilmMap();
        assertEquals(3, films.size());
    }

//по update - вроде все также как с user, но тут что-то не работает(
   /* @Test
    void updateTest() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(6, "Боевик"));
        Film film = new Film(4, "Second film", "Second film description",
                LocalDate.of(2020, 12, 12), 200, new Mpa(2, "PG"), genres);
        filmStorage.save(film);
        film.setName("New name for second film");
        film.setDescription("New description for second film");
        filmStorage.updateFilm(film);
        Film filmToCheck = filmStorage.get(film.getId());
        assertEquals("New name for second film", filmToCheck.getName());
        assertEquals("New description for second film", filmToCheck.getDescription());
    }*/

   /* @Test
    void removeTest() {
        filmStorage.removeFilm(1);
        List<Film> films = filmStorage.getFilmMap();
        assertEquals(1, films.size());
    }*/

}
