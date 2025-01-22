package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> getGenre(Long genreId);

    void setFilmGenres(Film film, List<Long> genreIds);

    void deleteFilmGenres(Film film, List<Long> genreIds);

    void load(Film film);

    void loadAll(List<Film> films);
}
