package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryUserStorage inMemoryUserStorage, InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }


    public void addLikeToFilm(long id, long userId) {
        final Film film = inMemoryFilmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден."));
        final User user = inMemoryUserStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        film.addLike(userId);
    }

    public void deleteLikeFromFilm(long id, long userId) {
        final Film film = inMemoryFilmStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден."));
        final User user = inMemoryUserStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        film.removeLike(userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return inMemoryFilmStorage.getAll().stream()
                .sorted(Comparator.comparing((Film film) -> getLikesCount(film)).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Integer getLikesCount(Film film) {
        return film.getLikes().size();
    }
}
