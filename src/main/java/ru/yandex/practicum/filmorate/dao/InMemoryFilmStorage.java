package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    public HashMap<Integer, Film> filmMap = new HashMap<>();
    public int generator = 0;

    @Override
    public Collection<Integer> getFilmMap() {
        return filmMap.keySet();
    }

    @Override
    public Film get(int filmId) {
        return filmMap.get(filmId);
    }

    @Override
    public Film save(Film film) {
        film.setId(++generator);
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
    public Set raitingFilm(int count){

        TreeSet<Film> raitingList = new TreeSet<>(Comparator.comparing(Film::getUserIdSize));

        for(Integer id: filmMap.keySet()){
            Film film = filmMap.get(id);
            raitingList.add(film);
        }

        if(raitingList.size()>count){
            int difference = count-raitingList.size();
            for(int i = 0; i<difference; i++){
                raitingList.remove(raitingList.last());
            }
        }

        return raitingList;
    }


}
