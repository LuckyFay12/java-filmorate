package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен HTTP-запрос на создание пользователя: {}", user);
        User createdUser = userStorage.create(user);
        log.info("Успешно обработан HTTP-запрос на создание пользователя: {}", user);
        return createdUser;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен HTTP-запрос на получение всех пользователей");
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение пользователя по id: {}", id);
        User user = userStorage.getById(id);
        log.debug("Найденный пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен HTTP-запрос на обновление пользователя с id {}", user.getId());
        User updatedUser = userStorage.update(user);
        log.info("Успешно обработан HTTP-запрос на обновление пользователя с id {}", user.getId());
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен HTTP-запрос на добавление друга c id {} у пользователя с id {}", friendId, id);
        userService.addFriend(id, friendId);
        log.info("Успешно обработан HTTP-запрос на добавление друга c id {} у пользователя с id {}", friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен HTTP-запрос на удаление друга с id {} у пользователя с id {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение всех друзей пользователь с id {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен HTTP-запрос на получение списка друзей пользователя с id {}, общих с пользователем с id {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
