package ru.yandex.practicum.filmorate.model;

import lombok.*;

/**
 * Mpa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Mpa {

    private int id;

    private String name;
}