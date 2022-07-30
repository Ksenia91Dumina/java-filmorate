package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    protected FilmController filmController = new FilmController();
    protected Film film1 = new Film();
    protected Film film2 = new Film();

    @BeforeEach
    void init() {
        film1.setName("Film1 name");
        film1.setDescription("description of Film1");
        film1.setReleaseDate(LocalDate.of(2000, 10, 10));
        film1.setDuration(135);
        film2.setName("Film2 name");
        film2.setDescription("description of Film2");
        film2.setReleaseDate(LocalDate.of(2010, 06, 15));
        film2.setDuration(170);
        filmController.createFilm(film1);
        filmController.createFilm(film2);
    }

    @Test
    void getAllFilms() {
        final Collection<Film> films = filmController.getAllFilms();
        assertNotNull(films);
        assertEquals(films.size(), 2, "Два фильма");
    }

    @Test
    void createFilm() {
        Film newFilm = new Film();
        newFilm.setName("New Film name");
        newFilm.setDescription("description of New Film");
        newFilm.setReleaseDate(LocalDate.of(2022, 06, 15));
        newFilm.setDuration(170);
        filmController.createFilm(newFilm);
        final Collection<Film> films = filmController.getAllFilms();
        assertNotNull(films);
        assertEquals(films.size(), 3, "Три фильма");
    }

    @Test
    void updateFilm() {
        film1.setName("New Name");
        filmController.updateFilm(film1);
        final Collection<Film> films = filmController.getAllFilms();
        assertNotNull(films);
        assertEquals("New Name", film1.getName(), "Фильм обновлен");
    }


}