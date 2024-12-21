package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(FilmService filmService, InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmService = filmService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> findAll() {
        Collection<Film> allFilms = inMemoryFilmStorage.getAll();
        log.info("Отправлен ответ Get /films с телом: {}", allFilms);
        return allFilms;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody final Film film) {
        log.info("Пришел Post запрос /films с телом: {}", film);
        Film curFilm = inMemoryFilmStorage.create(film);
        log.info("Отправлен ответ Post /films с телом: {}", curFilm);
        return curFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody final Film film) {
        log.info("Пришел Put запрос /films с телом: {}", film);
        Film curFilm = inMemoryFilmStorage.update(film);
        log.info("Отправлен ответ Put /films с телом: {}", curFilm);
        return curFilm;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilm(@PathVariable("id") long id) {
        Film curFilm = inMemoryFilmStorage.findFilmById(id).get();
        log.info("Отправлен ответ Get /films/{} с телом: {}", id, curFilm);
        return curFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void setLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        filmService.addLikeToFilm(id, userId);
        log.info("Отправлен ответ Put /films/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        filmService.deleteLikeFromFilm(id, userId);
        log.info("Отправлен ответ Delete /films/{}/like/{}", id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@Positive @RequestParam(defaultValue = "10") Integer count) {
        Collection<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Отправлен ответ Get /films/popular?count={} с телом: {}", count, popularFilms);
        return popularFilms;
    }
}
