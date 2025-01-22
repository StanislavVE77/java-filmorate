package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.info("Пришел GET запрос /films ");
        Collection<Film> allFilms = filmService.getFilms();
        log.info("Отправлен ответ на GET /films с телом: {}", allFilms);
        return allFilms;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable("id") long id) {
        log.info("Пришел GET запрос /films/{}", id);
        Film curFilm = filmService.getFilmById(id);
        log.info("Отправлен ответ на GET /films/{} с телом: {}", id, curFilm);
        return curFilm;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody final Film film) {
        log.info("Пришел POST запрос /films с телом: {}", film);
        Film curFilm = filmService.createFilm(film);
        log.info("Отправлен ответ на POST /films с телом: {}", curFilm);
        return curFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody final Film film) {
        log.info("Пришел PUT запрос /films с телом: {}", film);
        Film curFilm = filmService.updateFilm(film);
        log.info("Отправлен ответ на PUT /films с телом: {}", curFilm);
        return curFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void setLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Пришел PUT запрос /films/{}/like/{}", id, userId);
        filmService.addLikeToFilm(id, userId);
        log.info("Отправлен ответ на PUT /films/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Пришел DELETE запрос /films/{}/like/{}", id, userId);
        filmService.deleteLikeFromFilm(id, userId);
        log.info("Отправлен ответ на DELETE /films/{}/like/{}", id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@Positive @RequestParam(defaultValue = "10") Integer count) {
        log.info("Пришел GET запрос /films/popular?count={}", count);
        Collection<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Отправлен ответ на GET /films/popular?count={} с телом: {}", count, popularFilms);
        return popularFilms;
    }
}
