package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.Impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        try {
            return mpaStorage.getMpaById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Категория MPA с id " + id + " не найдена");
        }
    }
}
