package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;

    public FilmService(FilmDbStorage filmDbStorage, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage, UserDbStorage userDbStorage) {

        this.filmDbStorage = filmDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public Film getFilmById(long filmId) {
        Film film = filmDbStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + filmId + " не найден."));

        Mpa mpa = mpaDbStorage.findMpaById(film.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Рейтинг с  идентификатором " + film.getMpa().getId() + " не найден"));

        LinkedHashSet<Genre> genres = genreDbStorage.findByIds(List.of(filmId));

        film.setMpa(mpa);
        film.setGenres(genres);

        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>();
        Optional<Mpa> mpa;
        LinkedHashSet<Genre> genres;
        for (Film film : filmDbStorage.getAll()) {
            mpa = mpaDbStorage.findMpaById(film.getMpa().getId());
            if (mpa.isPresent()) {
                film.setMpa(mpa.get());
            }

            genres = genreDbStorage.findByIds(List.of(film.getId()));
            if (!genres.isEmpty()) {
                film.setGenres(genres);
            }

            films.add(film);
        }

        return films;
    }


    public Film createFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма должен быть указано");
        }
        if (film.getMpa() != null) {
            int mpaId = film.getMpa().getId();
            Mpa mpa = mpaDbStorage.findMpaById(mpaId)
                    .orElseThrow(() -> new ValidationException("Рейтинг с  идентификатором " + mpaId + " не найден"));
        }
        film = filmDbStorage.create(film);
        if (film.getGenres() != null) {
            List<Long> genreIds = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            try {
                genreDbStorage.setFilmGenres(film, genreIds);
            } catch (Exception e) {
                throw new ValidationException("Ошибка при добавлении несуществующего жанра");
            }
        }

        return film;
    }

    public Film updateFilm(Film film) {
        Film existFilm = filmDbStorage.findFilmById(film.getId())
                .orElseThrow(() -> new RuntimeException("Фильм с таким ID не найден"));

        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма должен быть указано");
        }

        if (film.getMpa() != null) {
            int mpaId = film.getMpa().getId();
            Mpa mpa = mpaDbStorage.findMpaById(mpaId)
                    .orElseThrow(() -> new ValidationException("Рейтинг с  идентификатором " + mpaId + " не найден"));
        }
        film = filmDbStorage.update(film);
        if (film.getGenres() != null) {
            List<Long> genreIds = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .toList();
            genreDbStorage.deleteFilmGenres(film, genreIds);
            genreDbStorage.setFilmGenres(film, genreIds);
        }
        return film;
    }

    public void addLikeToFilm(long id, long userId) {
        final Film film = filmDbStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден."));
        final User user = userDbStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLikeFromFilm(long id, long userId) {
        final Film film = filmDbStorage.findFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + id + " не найден."));
        final User user = userDbStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден."));
        filmDbStorage.deleteLike(id, userId);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmDbStorage.getAll().stream()
                .sorted(Comparator.comparing((Film film) -> filmDbStorage.getLikesCount(film.getId())).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAll();
    }

    public Mpa getMpaById(int mpaId) {
        Mpa mpa = mpaDbStorage.findMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинг с идентификатором " + mpaId + " не найден."));
        return mpa;
    }

    public List<Genre> getAllGenre() {
        return genreDbStorage.getAll();
    }

    public Genre getGenreById(Long genreId) {
        Genre genre = genreDbStorage.getGenre(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с идентификатором " + genreId + " не найден."));
        return genre;
    }

}
