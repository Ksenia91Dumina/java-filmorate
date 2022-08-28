/*package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;

    @Test
    void saveAndGetTest() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(4, "Триллер"));
        genres.add(new Genre(2, "Драма"));
        Film film = new Film(1,"New name", "New film description",
                LocalDate.of(2022,8,27),180, new Mpa(1, "G"), genres);

        genreStorage.setGenresForFilm(1, genres);
        filmStorage.save(film);
        List<Film> films = filmStorage.getFilmMap();
        assertEquals(1, films.size());
        Film filmToCheck = filmStorage.get(1);
        genreStorage.getGenresByFilmId(filmToCheck.getId());
        assertEquals("New name", filmToCheck.getName());
        assertEquals("New film description",filmToCheck.getDescription());
        assertEquals(LocalDate.of(2022,8,27),filmToCheck.getReleaseDate());
        assertEquals(genres,filmToCheck.getGenres());
        assertEquals(new Mpa(1, "G"), filmToCheck.getMpa());
    }


    @Test
    void updateTest() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(6, "Боевик"));
        Film film = new Film(2,"Second film","Second film description",
                LocalDate.of(2020,12,12),200, new Mpa(2, "PG"),genres);
        filmStorage.save(film);
        film.setName("New name for second film");
        film.setDescription("New description for second film");
        filmStorage.updateFilm(film);
        Film filmToCheck = filmStorage.get(2);
        assertEquals("New name for second film", filmToCheck.getName());
        assertEquals("New description for second film",filmToCheck.getDescription());
    }

    @Test
    void getFilmMapTest() {
        List<Film> films = filmStorage.getFilmMap();
        assertEquals(2, films.size());
    }

    @Test
    void removeTest() {
       filmStorage.removeFilm(1);
        List<Film> films = filmStorage.getFilmMap();
        assertEquals(1, films.size());
    }

}*/
