package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class User {

    @NotNull(groups = {Update.class})
    private long id;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    private String email;

    @NotBlank(groups = {Create.class})
    @Pattern(regexp = "^[a-zA-Z0-9-_.]{3,}$", groups = {Create.class, Update.class})
    private String login;

    private String name;

    @Past(groups = {Create.class, Update.class})
    private LocalDate birthday;

    final Set<Long> friends = new HashSet<>();
}