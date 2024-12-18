package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.ValidationException;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        log.info("Отправлен ответ Get /films с телом: {}", filmService.getAll());
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody final Film film) {
        log.info("Пришел Post запрос /films с телом: {}", film);
        Film curFilm = filmService.create(film);
        log.info("Отправлен ответ Post /films с телом: {}", curFilm);
        return curFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody final Film film) {
        log.info("Пришел Put запрос /films с телом: {}", film);
        Film curFilm = filmService.update(film);
        log.info("Отправлен ответ Put /films с телом: {}", curFilm);
        return curFilm;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable("id") long id) {
        Film curFilm = filmService.findFilmById(id).get();
        log.info("Отправлен ответ Get /films/{} с телом: {}", id, curFilm);
        return curFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film setLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        Film curFilm = filmService.addLikeToFilm(id, userId);
        log.info("Отправлен ответ Put /films/{}/like/{} с телом: {}", id, userId, curFilm);
        return curFilm;
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public  Film deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        Film curFilm = filmService.deleteLikeFromFilm(id, userId);
        log.info("Отправлен ответ Delete /films/{}/like/{} с телом: {}", id, userId, curFilm);
        return curFilm;
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new ValidationException("Парамерт count должен быть положительным.");
        }
        Collection<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Отправлен ответ Get /films/popular?count={} с телом: {}", count, popularFilms);
        return popularFilms;
    }
}
