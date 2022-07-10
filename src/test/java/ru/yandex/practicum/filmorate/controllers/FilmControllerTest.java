package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    protected FilmController filmController;
    protected Film film1;
    protected Film film2;

    @BeforeEach
    void init() {
        filmController = new FilmController();
        film1 = new Film(filmController.getUniqueID(), "Film1 name", "description of Film1",
                LocalDate.of(2000, 10, 10), 135);
        film2 = new Film(filmController.getUniqueID(), "Film2 name", "description of Film2",
                LocalDate.of(2010, 06, 15), 170);
        filmController.createFilm(film1);
        filmController.createFilm(film2);
    }

    @Test
    void getAllFilms() {
        final Map<Integer, Film> films = filmController.getAllFilms();
        assertNotNull(films);
        assertEquals(films.size(), 2, "Два фильма");
        assertEquals(film1, films.get(film1.getFilmId()));
        assertEquals(film2, films.get(film2.getFilmId()));
    }

    @Test
    void createFilm() {
        Film newFilm = new Film(filmController.getUniqueID(), "New Film name",
                "description of New Film",
                LocalDate.of(2022, 06, 15), 170);
        filmController.createFilm(newFilm);
        final Map<Integer, Film> films = filmController.getAllFilms();
        assertNotNull(films);
        assertEquals(films.size(), 3, "Три фильма");
        assertEquals(newFilm, films.get(newFilm.getFilmId()));
    }

    @Test
    void updateFilm() {
        film1.setFilmName("New Name");
        filmController.updateFilm(film1);
        final Map<Integer, Film> films = filmController.getAllFilms();
        assertNotNull(films);
        assertEquals("New Name", film1.getFilmName(), "Фильм обновлен");
    }

    @Test
    void getUniqueID() {
        assertNotEquals(film1.getFilmId(), film2.getFilmId(), "У фильмов разные id");
    }

}