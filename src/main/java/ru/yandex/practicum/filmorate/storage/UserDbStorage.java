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
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = :id";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = :email";
    private static final String INSERT_QUERY = "INSERT INTO users(login, email, name, birthday) VALUES (:login, :email, :name, :birthday)";
    private static final String UPDATE_QUERY = "UPDATE users SET login = :login, email = :email, name = :name, birthday = :birthday WHERE id = :id";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends(user_id, friend_id) VALUES (:user_id, :friend_id)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id";
    private static final String FIND_USER_FRIENDS_QUERY = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = :user_id)";

    private final NamedParameterJdbcOperations jdbc;
    protected final RowMapper<User> mapper;

    @Override
    public User create(User user)
    {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        jdbc.update(INSERT_QUERY, params, keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKeyAs(Long.class));

        return user;
    }

    @Override
    public User update(User user)
    {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", user.getLogin());
        params.addValue("email", user.getEmail());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        params.addValue("id", user.getId());
        jdbc.update(UPDATE_QUERY, params);
        return user;
    }

    @Override
    public Collection<User> getAll()
    {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public Optional<User> findUserById(long id)
    {
        try {
            SqlParameterSource params = new MapSqlParameterSource("id", id);
            User result = jdbc.queryForObject(FIND_BY_ID_QUERY, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public Optional<User> findUserByEmail(String email)
    {
        try {
            SqlParameterSource params = new MapSqlParameterSource("email", email);
            User result = jdbc.queryForObject(FIND_BY_EMAIL_QUERY, params, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public  void  addFriend(Long id, Long friend_id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", friend_id);
        jdbc.update(INSERT_FRIEND_QUERY, params);
    }

    public  void  deleteFriend(Long id, Long friend_id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", friend_id);
        jdbc.update(DELETE_FRIEND_QUERY, params);
    }

    public Collection<User> getFriends(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        return jdbc.query(FIND_USER_FRIENDS_QUERY, params, mapper);
    }
}
