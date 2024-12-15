package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    protected long seq = 0L;

    public Collection<User> get() {
        return users.values();
    }

    public User create(User user) {
        user.setId(generateId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getName() == null || newUser.getName().isEmpty()) {
                if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) {
                    oldUser.setName(newUser.getLogin());
                }
            } else {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getLogin() != null) {
                oldUser.setLogin(newUser.getLogin());
            }
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            return oldUser;
        }
        log.error("User id: {} - not found.", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long generateId() {
        return ++seq;
    }

}
