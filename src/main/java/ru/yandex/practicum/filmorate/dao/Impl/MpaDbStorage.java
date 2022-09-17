package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor

public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("rating_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Mpa getMpaById(int mpa_id) {
        String sqlQuery = "SELECT * FROM MPA WHERE MPA.MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, mpa_id);
    }

}