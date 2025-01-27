package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDbStorage userDbStorage;

    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public User getUserById(long userId) {
        return userDbStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public List<User> getUsers() {
        return userDbStorage.getAll()
                .stream()
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new javax.validation.ValidationException("Имейл должен быть указан");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new javax.validation.ValidationException("Логин должен быть указан");
        }
        Optional<User> alreadyExistUser = userDbStorage.findUserByEmail(user.getEmail());
        if (alreadyExistUser.isPresent()) {
            throw new ValidationException("Данный имейл уже используется");
        }
        user = userDbStorage.create(user);
        return user;
    }

    public User updateUser(User user) {
        User updatedUser = userDbStorage.findUserById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        updatedUser = userDbStorage.update(user);
        return updatedUser;
    }

    public void addFriendToUser(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Нельзя добавить в друзья самого себя.");
        }
        final User user = userDbStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User friendUser = userDbStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + friendId + " не найден."));
        userDbStorage.addFriend(id, friendId);
    }

    public void deleteFriendFromUser(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Идентификаторы пользователя и друга совпадают.");
        }
        final User user = userDbStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User friendUser = userDbStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Удаляемый из друзей пользователь с id = " + friendId + " не найден."));
        userDbStorage.deleteFriend(id, friendId);
    }

    public Collection<User> getUserFriends(long id) {
        final User user = userDbStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        Collection<User> userFriends = userDbStorage.getFriends(id);
        return userFriends;
    }

    public Collection<User> getUsersCommonFriends(long id, long otherId) {
        final User user = userDbStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User otherUser = userDbStorage.findUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + otherId + " не найден."));
        Collection<User> userFriends = userDbStorage.getFriends(id);
        Collection<User> otherUserFriends = userDbStorage.getFriends(otherId);
        userFriends.retainAll(otherUserFriends);
        return userFriends;
    }
}
