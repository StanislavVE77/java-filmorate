package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@Validated
@RequiredArgsConstructor
public class MpaController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> getAll() {
        List<Mpa> allMpa = filmService.getAllMpa();
        log.info("Отправлен ответ Get /mpa с телом: {}", allMpa);
        return allMpa;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getMpa(@PathVariable("id") int id) {
        Mpa curMpa = filmService.getMpaById(id);
        log.info("Отправлен ответ Get /mpa/{} с телом: {}", id, curMpa);
        return curMpa;
    }
}
