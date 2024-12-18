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
public class UserService  extends InMemoryUserStorage {

    public User addFriendToUser(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Нельзя добавить в друзья самого себя.");
        }
        final User user = findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User friendUser = findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + friendId + " не найден."));
        Set<Long> curOtherFriends = friendUser.getFriends();
        if (curOtherFriends == null) {
            curOtherFriends = new HashSet<>();
        }
            curOtherFriends.add(id);
        friendUser.setFriends(curOtherFriends);

        Set<Long> curFriends = user.getFriends();
        if (curFriends == null) {
            curFriends = new HashSet<>();
        }
        curFriends.add(friendId);
        user.setFriends(curFriends);
        return user;
    }

    public User deleteFriendFromUser(long id, long friendId) {
        if (id == friendId) {
            throw new ValidationException("Идентификаторы пользователя и друга совпадают.");
        }
        final User user = findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User friendUser = findUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + friendId + " не найден."));
        Set<Long> curOtherFriends = friendUser.getFriends();
        if (curOtherFriends != null) {
            curOtherFriends.remove(id);
            friendUser.setFriends(curOtherFriends);
        }
        Set<Long> curFriends = user.getFriends();
        if (curFriends != null) {
            curFriends.remove(friendId);
            user.setFriends(curFriends);
        }
        return user;
    }

    public Collection<User> getUserFriends(long id) {
        Collection<User> userFriends = new HashSet<>();
        final User user = findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        Set<Long> curFriends = user.getFriends();
        if (curFriends != null) {
            for (Long tempId : curFriends) {
                userFriends.add(findUserById(tempId).get());
            }
        }
        return userFriends;
    }

    public Collection<User> getUsersCommonFriends(long id, long otherId) {
        Collection<User> commonFriends = new HashSet<>();
        final User user = findUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден."));
        final User otherUser = findUserById(otherId)
                .orElseThrow(() -> new NotFoundException("Добавляемый в друзья пользователь с id = " + otherId + " не найден."));
        Set<Long> friends = user.getFriends();
        if (friends == null) {
            friends = new HashSet<>();
        }
        Set<Long> otherFriends = otherUser.getFriends();
        if (otherFriends == null) {
            otherFriends = new HashSet<>();
        }
        for (Long oneId : friends) {
            for (Long twoId : otherFriends) {
                if (oneId == twoId) {
                    commonFriends.add(findUserById(oneId).get());
                }
            }
        }
        return commonFriends;
    }

}
