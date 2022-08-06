package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {

    public InMemoryFilmStorage inMemoryFilmStorage;
    public Film film1 = new Film();
    public Film film2 = new Film();
    public int uniqueId = 1;

    @BeforeEach
    void init() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        film1.setId(++uniqueId);
        film1.setName("Film1 name");
        film1.setDescription("description of Film1");
        film1.setReleaseDate(LocalDate.of(2000, 10, 10));
        film1.setDuration(135);
        film2.setId(++uniqueId);
        film2.setName("Film2 name");
        film2.setDescription("description of Film2");
        film2.setReleaseDate(LocalDate.of(2010, 06, 15));
        film2.setDuration(170);
        inMemoryFilmStorage.save(film1);
        inMemoryFilmStorage.save(film2);
    }

    @Test
    void getFilmMapTest() {
        final Collection<Film> films = inMemoryFilmStorage.getFilmMap();
        assertNotNull(films);
        assertEquals(films.size(), 2, "Два фильма");
    }

    @Test
    void getTest() {
        Film filmToCheck = inMemoryFilmStorage.get(1);
        assertEquals(film1.getName(), filmToCheck.getName(), "Имена равны");
        assertEquals(film1, filmToCheck, "Фильмы равны");
    }

    @Test
    void saveTest() {
        Film newFilm = new Film();
        newFilm.setId(++uniqueId);
        newFilm.setName("New Film name");
        newFilm.setDescription("description of New Film");
        newFilm.setReleaseDate(LocalDate.of(2022, 06, 15));
        newFilm.setDuration(170);
        inMemoryFilmStorage.save(newFilm);
        final Collection<Film> films = inMemoryFilmStorage.getFilmMap();
        assertNotNull(films);
        assertEquals(films.size(), 3, "Три фильма");
    }


    @Test
    void updateFilmTest() {
        film1.setName("New Name");
        inMemoryFilmStorage.updateFilm(film1);
        assertEquals("New Name", film1.getName(), "Фильм обновлен");
    }

    @Test
    void addLikeTest() {
        User user = new User();
        inMemoryFilmStorage.addLike(user, film1);
        assertEquals(1, film1.getUserIds().size(), "Лайк поставлен");
    }

    @Test
    void deleteLikeTest() {
        User user = new User();
        inMemoryFilmStorage.deleteLike(user, film1);
        assertEquals(0, film1.getUserIds().size(), "Лайк удален");
    }

    @Test
    void raitingFilmTest() {
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        inMemoryFilmStorage.addLike(user1, film2);
        inMemoryFilmStorage.addLike(user2, film2);
        inMemoryFilmStorage.addLike(user3, film2);
        inMemoryFilmStorage.addLike(user1, film1);
        List<Film> raitingFilms = inMemoryFilmStorage.raitingFilm(3);
        Film filmToCheck = raitingFilms.get(1);
        assertEquals(film2.getName(), filmToCheck.getName(), "Имена равны");
        assertEquals(film2, filmToCheck, "Фильмы равны");
    }
}