package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = :id";
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private final NamedParameterJdbcOperations jdbc;
    protected final RowMapper<Mpa> mapper;

    public  Optional<Mpa> findMpaById(int id) {
        try {
            SqlParameterSource params = new MapSqlParameterSource("id", id);
            Mpa result = jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public List<Mpa> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }
}
