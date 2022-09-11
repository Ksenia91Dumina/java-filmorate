package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")

public class Mpa {

    private int id;
    private String name;

}
