package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Genre {
    private int id;
    private String name;
}