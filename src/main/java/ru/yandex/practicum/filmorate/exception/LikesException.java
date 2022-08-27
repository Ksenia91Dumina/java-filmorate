package ru.yandex.practicum.filmorate.exception;

import org.springframework.dao.DataAccessException;

public class LikesException extends DataAccessException {
    public LikesException(String message) {
        super(message);
    }
}
