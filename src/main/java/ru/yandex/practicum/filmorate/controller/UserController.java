package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Create;
import ru.yandex.practicum.filmorate.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        log.info("Пришел GET запрос /users ");
        Collection<User> allUsers = userService.getUsers();
        log.info("Отправлен ответ на GET /users с телом: {}", allUsers);
        return allUsers;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") long id) {
        log.info("Пришел GET запрос /users/{}", id);
        User curUser = userService.getUserById(id);
        log.info("Отправлен ответ на GET /users/{} с телом: {}", id, curUser);
        return curUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Create.class)  @RequestBody final User user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        User curUser = userService.createUser(user);
        log.info("Отправлен ответ на POST /users с телом: {}", curUser);
        return curUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Validated(Update.class) @RequestBody final User user) {
        log.info("Пришел PUT запрос /users с телом: {}", user);
        User curUser = userService.updateUser(user);
        log.info("Отправлен ответ на PUT /users с телом: {}", curUser);
        return curUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Пришел PUT запрос /users/{}/friends/{}", id, friendId);
        userService.addFriendToUser(id, friendId);
        log.info("Отправлен ответ на PUT /users/{}/friends/{}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Пришел DELETE запрос /users/{}/friends/{}", id, friendId);
        userService.deleteFriendFromUser(id, friendId);
        log.info("Отправлен ответ на DELETE /users/{}/friends/{}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable("id") long id) {
        log.info("Пришел GET запрос /users/{}/friends", id);
        Collection<User> userFriends = userService.getUserFriends(id);
        log.info("Отправлен ответ на GET /users/{}/friends с телом: {}", id, userFriends);
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        log.info("Пришел GET запрос /users/{}/friends/common/{}", id, otherId);
        Collection<User> commonFriends = userService.getUsersCommonFriends(id, otherId);
        log.info("Отправлен ответ на GET /users/{}/friends/common/{} с телом: {}", id, otherId, commonFriends);
        return commonFriends;
    }
}
