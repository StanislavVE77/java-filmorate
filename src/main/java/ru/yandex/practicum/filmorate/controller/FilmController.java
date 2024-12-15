package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final FilmRepository repo = new FilmRepository();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.info("Отправлен ответ Get /films с телом: {}", repo.get());
        return repo.get();
    }

        @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        log.info("Пришел Post запрос /films с телом: {}", film);
        Film respFilm = repo.create(film);
        log.info("Отправлен ответ Post /films с телом: {}", respFilm);
        return respFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        log.info("Пришел Put запрос /films с телом: {}", film);
        Film respFilm = repo.update(film);
        log.info("Отправлен ответ Put /films с телом: {}", respFilm);
        return respFilm;
    }
}
