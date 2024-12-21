package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */
@Data
@Builder
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

    private final Set<Long> friends = new HashSet<>();

    public void addUser(long id) {
        friends.add(id);
    }

    public void removeUser(long id) {
        friends.remove(id);
    }

}