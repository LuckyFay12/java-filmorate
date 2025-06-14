package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendStorage friendsStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User update(User user) {
        Long id = user.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id пользователя не может быть null");
        }
        if (getById(id) == null) {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        return userStorage.update(user);
    }

    public User getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id пользователя не может быть null");
        }
        return userStorage.getById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        friendsStorage.addFriend(userId, friendId);
        log.info("Пользователь с именем {} добавил в друзья пользователя c именем {}", user.getName(), friend.getName());
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        friendsStorage.deleteFriend(userId, friendId);
        log.info("Пользователь с именем {} удалил из друзей пользователя c именем {}", user.getName(), friend.getName());
    }

    public List<User> getUserFriends(Long userId) {
        User user = getById(userId);
        return friendsStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        User user1 = getById(userId1);
        User user2 = getById(userId2);
        return friendsStorage.getCommonFriends(userId1, userId2);
    }
}
