package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private int id;
    @NotNull(message = "Почта не может быть пустой")
    @Email(message = "Ошибка формата электронной почты")
    private String email;
    @NotNull(message = "Логин не может быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;

}