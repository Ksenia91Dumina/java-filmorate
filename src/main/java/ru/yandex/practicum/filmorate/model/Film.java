package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    int filmId;
    @NotBlank (message = "Название не может быть пустым")
    String filmName;
    @NotBlank
    String filmDescription;
    @Past
    LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    int duration;

}
