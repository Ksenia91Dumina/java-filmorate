package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;

    @JsonIgnore
    Set<Integer> userIds = new HashSet<Integer>();

    public static int getUserIdSize(Film film){
        return film.getUserIds().size();
    }
}
