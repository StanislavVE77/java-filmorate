package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private static final String FIND_BY_FILMID_QUERY = "SELECT * FROM genre g JOIN film_genre fg  WHERE g.id = fg.genre_id AND fg.film_id = :id";
    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO film_genre(film_id, genre_id) VALUES (:film_id, :genre_id)";
    private static final String DELETE_GENRE_FROM_FILM_QUERY = "DELETE FROM film_genre WHERE film_id = :film_id AND genre_id :genre_id)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = :id";
    private final NamedParameterJdbcOperations jdbc;
    protected final RowMapper<Genre> mapper;

    @Override
    public List<Genre> findGenresByFilmId(Long id) {
        try {
            SqlParameterSource params = new MapSqlParameterSource("id", id);
            List<Genre> result = jdbc.query(FIND_BY_FILMID_QUERY, params, mapper);
            return result;
        } catch (EmptyResultDataAccessException ignored) {
            return List.of();
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Genre> getGenre(Long genreId) {
        try {
            SqlParameterSource params = new MapSqlParameterSource("id", genreId);
            Genre result = jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void setFilmGenres(Film film, List<Long> genreIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        for (Long genreId : genreIds) {
                Genre curGenre = getGenre(genreId).get();
                params.addValue("film_id", film.getId());
                params.addValue("genre_id", genreId);
                jdbc.update(ADD_GENRE_TO_FILM_QUERY, params);
        }
    }

    @Override
    public void deleteFilmGenres(Film film, List<Long> genreIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        for (Long genreId : genreIds) {
            Genre curGenre = getGenre(genreId).get();
            List<Genre> listGenres = getAll();
            if (curGenre != null && listGenres.contains(curGenre)) {

                params.addValue("film_id", film.getId());
                params.addValue("genre_id", genreId);
                jdbc.update(DELETE_GENRE_FROM_FILM_QUERY, params);
            } else {
                throw new ValidationException("Жанра с идентификатором " + genreId + " нет");
            }
        }
    }

    @Override
    public LinkedHashSet<Genre> findByIds(List<Long> ids) {
        LinkedHashSet<Genre> genreList = new LinkedHashSet<>();
        for (Long id : ids) {
            List<Genre> genres = findGenresByFilmId(id);
            for (Genre genre : genres) {
                genreList.add(genre);
            }
        }
        return genreList;
    }
}
