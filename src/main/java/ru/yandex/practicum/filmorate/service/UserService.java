package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriendToUser(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Нельзя добавить в друзья самого себя.");
        }
        final User user = inMemoryUserStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User friendUser = inMemoryUserStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + friendId + " не найден."));
        user.addUser(friendId);
        friendUser.addUser(id);
    }

    public void deleteFriendFromUser(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Идентификаторы пользователя и друга совпадают.");
        }
        final User user = inMemoryUserStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User friendUser = inMemoryUserStorage.findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Удаляемый из друзей пользователь с id = " + friendId + " не найден."));
        user.removeUser(friendId);
        friendUser.removeUser(id);
    }

    public Collection<User> getUserFriends(long id) {
        Collection<User> userFriends = new HashSet<>();
        final User user = inMemoryUserStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        Set<Long> curFriends = user.getFriends();
        for (Long tempId : curFriends) {
            userFriends.add(inMemoryUserStorage.findUserById(tempId).get());
        }
        return userFriends;
    }

    public Collection<User> getUsersCommonFriends(long id, long otherId) {
        Collection<User> commonFriends = new HashSet<>();
        final User user = inMemoryUserStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User otherUser = inMemoryUserStorage.findUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + otherId + " не найден."));
        Set<Long> friends = user.getFriends();
        Set<Long> otherFriends = otherUser.getFriends();
        friends.retainAll(otherFriends);
        for (Long commonId : friends) {
            commonFriends.add(inMemoryUserStorage.findUserById(commonId).get());
        }
        return commonFriends;
    }
}
