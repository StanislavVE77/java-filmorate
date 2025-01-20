package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = :id";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM films WHERE name = :name";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, duration, releaseDate) VALUES (:name, :description, :duration, :releaseDate)";
    private static final String INSERT_WITH_MPA_QUERY = "INSERT INTO films(name, description, duration, releaseDate, mpa_id) VALUES (:name, :description, :duration, :releaseDate, :mpa_id)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = :name, description = :description, duration = :duration, releaseDate = :releaseDate, mpa_id = :mpa_id WHERE id = :id";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (:film_id, :user_id)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = :film_id AND user_id = :user_id";
    private static final String GET_LIKE_COUNT_QUERY = "SELECT count(*) FROM film_likes WHERE film_id = :film_id";

    private final NamedParameterJdbcOperations jdbc;
    protected final RowMapper<Film> mapper;

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("duration", film.getDuration());
        params.addValue("releaseDate", film.getReleaseDate());
        if (film.getMpa() != null) {
            params.addValue("mpa_id", film.getMpa().getId());
            jdbc.update(INSERT_WITH_MPA_QUERY, params, keyHolder, new String[]{"id"});
        } else {
            jdbc.update(INSERT_QUERY, params, keyHolder, new String[]{"id"});
        }

        film.setId(keyHolder.getKeyAs(Long.class));

        return film;
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("duration", film.getDuration());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("id", film.getId());
        if (film.getMpa() != null) {
            params.addValue("mpa_id", film.getMpa().getId());
        }
        jdbc.update(UPDATE_QUERY, params);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<Film> findFilmById(long id) {
        try {
            SqlParameterSource params = new MapSqlParameterSource("id", id);
            Film result = jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> findFilmByName(String name) {
        try {
            SqlParameterSource params = new MapSqlParameterSource("name", name);
            Film result = jdbc.queryForObject(FIND_BY_NAME_QUERY, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        params.addValue("user_id", userId);
        jdbc.update(INSERT_LIKE_QUERY, params);

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        params.addValue("user_id", userId);
        jdbc.update(DELETE_LIKE_QUERY, params);

    }

    @Override
    public Integer getLikesCount(Long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        return jdbc.queryForObject(GET_LIKE_COUNT_QUERY, params, Integer.class);
    }

}

