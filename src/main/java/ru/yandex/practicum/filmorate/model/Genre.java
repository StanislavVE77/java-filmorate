package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import jakarta.validation.constraints.NotBlank;

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