package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
@Validated
@RequiredArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAll() {
        log.info("Пришел GET запрос /genres ");
        List<Genre> allGenre = filmService.getAllGenre();
        log.info("Отправлен ответ на GET /genres с телом: {}", allGenre);
        return allGenre;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenre(@PathVariable("id") Long id) {
        log.info("Пришел GET запрос /genres/{} ", id);
        Genre curGenre = filmService.getGenreById(id);
        log.info("Отправлен ответ на GET /genres/{} с телом: {}", id, curGenre);
        return curGenre;
    }
}
