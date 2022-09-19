package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
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
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;

}