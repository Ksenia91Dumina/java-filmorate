package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class InMemoryFilmStorage implements FilmStorage {

    public HashMap<Integer, Film> filmMap = new HashMap<>();

    @Override
    public List<Film> getFilmMap() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film get(int filmId) {
        Film film = new Film();
        if (!filmMap.isEmpty()) {
            if (filmMap.containsKey(filmId)) {
                film = filmMap.get(filmId);
            }
        }
        return film;
    }

    @Override
    public Film save(Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean removeFilm(Film film) {
        filmMap.remove(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(User user, Film film) {
        film.getUserIds().add(user.getId());
    }

    @Override
    public void deleteLike(User user, Film film) {
        film.getUserIds().remove(user.getId());
    }

    @Override
    public List<Film> raitingFilm(int count) {

        List<Film> raitingFilms = filmMap.values().stream()
                .sorted(Comparator.comparingInt(film0 -> film0.getUserIds().size()))
                .limit(count)
                .collect(Collectors.toList());

        return raitingFilms;
    }

}