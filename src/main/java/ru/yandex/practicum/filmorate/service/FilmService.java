package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService extends InMemoryFilmStorage {
    UserService userService;

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }


    public Film addLikeToFilm(long id, long userId) {
        final Film film = findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден."));
        final User user = userService.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        Set<Long> filmLikes = film.getLikes();
        if (filmLikes == null) {
            filmLikes = new HashSet<>();
        }
        filmLikes.add(userId);
        film.setLikes(filmLikes);
        return film;
    }

    public Film deleteLikeFromFilm(long id, long userId) {
        final Film film = findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден."));
        Set<Long> filmLikes = film.getLikes();
        if (filmLikes != null) {
            filmLikes.remove(userId);
            film.setLikes(filmLikes);
        }
        return film;
    }

    public Collection<Film> getPopularFilms(Integer count) {

        return getAll().stream()
                .sorted(Comparator.comparing((Film film) -> getLikesCount(film)).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Integer getLikesCount(Film film) {
        return film.getLikes().size();
    }
}
