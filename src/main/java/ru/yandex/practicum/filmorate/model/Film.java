package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Film.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Film {

    @NotNull(groups = {Update.class})
    private long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Size(max = 200, groups = {Create.class, Update.class})
    private String description;

    Mpa mpa;

    LinkedHashSet<Genre> genres;

    @NotNull(groups = {Create.class})
    @ReleaseDate(groups = {Create.class})
    private LocalDate releaseDate;

    @Positive(groups = {Create.class, Update.class})
    private Integer duration;

}
