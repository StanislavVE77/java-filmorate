package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> get() {
        return films.values();
    }

    public void create(Film film) {
        log.info("Create film: {} - started.", film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Create film: {} - finished.", film);
    }

    public Film update(Film newFilm) {
        log.info("Update film: {} - started.", newFilm);
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
            }
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
            }
            log.info("Update film: {} - finished.", newFilm);
            return oldFilm;
        }
        log.error("Film id: {} - not found.", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
