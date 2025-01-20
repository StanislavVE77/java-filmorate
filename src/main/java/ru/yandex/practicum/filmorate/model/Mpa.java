package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Mpa.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Mpa {

    @NotNull(groups = {Create.class, Update.class})
    private int id;
    @NotBlank(groups = {Create.class, Update.class})
    private String name;

}