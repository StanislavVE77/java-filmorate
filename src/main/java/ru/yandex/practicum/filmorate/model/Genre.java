package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Genre.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Genre {

    @NotNull(groups = {Create.class, Update.class})
    private long id;
    @NotBlank(groups = {Create.class, Update.class})
    private String name;
}