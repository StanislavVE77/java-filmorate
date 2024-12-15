package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserRepository repo = new UserRepository();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Отправлен ответ Get /users с телом: {}", repo.get());
        return repo.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Create.class) @RequestBody final User user) {
        log.info("Пришел Post запрос /users с телом: {}", user);
        User respUser = repo.create(user);
        log.info("Отправлен ответ Post /users с телом: {}", respUser);
        return respUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Validated(Update.class) @RequestBody User user) {
        log.info("Пришел Put запрос /users с телом: {}", user);
        User respUser = repo.update(user);
        log.info("Отправлен ответ Put /users с телом: {}", respUser);
        return respUser;
    }
}
