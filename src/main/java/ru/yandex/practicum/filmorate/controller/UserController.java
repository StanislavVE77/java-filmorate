package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(UserService userService, InMemoryUserStorage inMemoryUserStorage) {
        this.userService = userService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        Collection<User> allUsers = inMemoryUserStorage.getAll();
        log.info("Отправлен ответ Get /users с телом: {}", allUsers);
        return allUsers;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Create.class) @RequestBody final User user) {
        log.info("Пришел Post запрос /users с телом: {}", user);
        User curUser = inMemoryUserStorage.create(user);
        log.info("Отправлен ответ Post /users с телом: {}", curUser);
        return curUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Validated(Update.class) @RequestBody final User user) {
        log.info("Пришел Put запрос /users с телом: {}", user);
        User curUser = inMemoryUserStorage.update(user);
        log.info("Отправлен ответ Put /users с телом: {}", curUser);
        return curUser;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") long id) {
        User curUser = inMemoryUserStorage.findUserById(id).get();
        log.info("Отправлен ответ Get /users/{} с телом: {}", id, curUser);
        return curUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.addFriendToUser(id, friendId);
        log.info("Отправлен ответ Put /users/{}/friends/{}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.deleteFriendFromUser(id, friendId);
        log.info("Отправлен ответ Delete /users/{}/friends/{}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable("id") long id) {
        Collection<User> userFriends = userService.getUserFriends(id);
        log.info("Отправлен ответ Get /{}/friends с телом: {}", id, userFriends);
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        Collection<User> commonFriends = userService.getUsersCommonFriends(id, otherId);
        log.info("Отправлен ответ Get /{}/friends/common/{} с телом: {}", id, otherId, commonFriends);
        return commonFriends;
    }
}
