package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Название не может быть пустым")
    private String name;
    @NotNull
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;

    private MPA mpa;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, MPA mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @JsonIgnore
    private Set<Integer> userIds = new HashSet<Integer>();

    private Set<Genre> genres;

}