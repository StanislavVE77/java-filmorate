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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private static final String FIND_BY_FILMID_QUERY = "SELECT * FROM genre g JOIN film_genre fg  WHERE g.id = fg.genre_id AND fg.film_id = :id";
    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO film_genre(film_id, genre_id) VALUES (:film_id, :genre_id)";
    private static final String DELETE_GENRE_FROM_FILM_QUERY = "DELETE FROM film_genre WHERE film_id = :film_id AND genre_id :genre_id)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = :id";
    private static final String LOAD_ALL_QUERY = "SELECT * FROM genre g, film_genre fg WHERE fg.genre_id = g.id AND fg.film_id IN ( SELECT id FROM films )";
    private static final String LOAD_ONE_QUERY = "SELECT * FROM genre g, film_genre fg WHERE fg.genre_id = g.id AND fg.film_id = :id";
    private final NamedParameterJdbcOperations jdbc;
    protected final RowMapper<Genre> mapper;

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
    public void load(Film film) {
        SqlParameterSource params = new MapSqlParameterSource("id", film.getId());
        jdbc.query(LOAD_ONE_QUERY, params,
            (rs) -> {
                Genre genre = makeGenre(rs, 0);
                film.addGenre(genre);
            }
        );
    }

    @Override
    public void loadAll(List<Film> films) {
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        jdbc.query(LOAD_ALL_QUERY,  (rs) -> {
                final Film film = filmById.get(rs.getLong("film_id"));
                Genre genre = makeGenre(rs, 0);
                film.addGenre(genre);
            }
        );
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getLong("genre_id"),
                rs.getString("name"));
    }
/*
    @Override
    public void loadAll(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        Object[] filmIds = films.stream().map(Film::getId).toArray();
        final String sqlQuery = "select * from GENRE g, FILM_GENRE fg where fg.GENRE_ID = g.ID AND fg.FILM_ID in (" + inSql + ")";
        jdbcTemplate.query(
            sqlQuery,
            (rs) -> {
                     final Film film = filmById.get(rs.getLong("film_id"));
                    film.addGenre(makeGenre(rs, 0));
            },
            filmIds
        );
    }
*/

}
