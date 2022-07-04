package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

int id;
@NotBlank (message = "Почта не может быть пустой")
@Email(message = "Ошибка формата электронной почты")
String email;
@NotBlank (message = "Логин не может быть пустым")
String login;
String name;
@Past (message = "Дата не может быть в будущем")
LocalDate birthday;

}
