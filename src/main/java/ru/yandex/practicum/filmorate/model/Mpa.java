package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import jakarta.validation.constraints.NotBlank;

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