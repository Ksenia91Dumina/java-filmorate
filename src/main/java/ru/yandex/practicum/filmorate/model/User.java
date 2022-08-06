package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    @NotNull(message = "Почта не может быть пустой")
    @Email(message = "Ошибка формата электронной почты")
    private String email;
    @NotNull(message = "Логин не может быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friendIds = new HashSet<Integer>();

}
