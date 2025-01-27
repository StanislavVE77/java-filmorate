package ru.yandex.practicum.filmorate.model;

import lombok.*;

/**
 * Genre.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Genre {

    private long id;

    private String name;

}