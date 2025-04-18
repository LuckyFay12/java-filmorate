package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
        log.info("Пользователь с id {} добавил в друзья пользователя c id {}", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.update(user);
        userStorage.update(friend);
        log.info("Пользователь с id {} удалил из друзей пользователя c id {}", userId, friendId);
    }

    public List<User> getUserFriends(Long userId) {
        User user = userStorage.getById(userId);
        return user.getFriends().stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        Set<Long> friends1 = userStorage.getById(userId1).getFriends();
        Set<Long> friends2 = userStorage.getById(userId2).getFriends();
        return userStorage.getAll().stream()
                .filter(user -> friends1.contains(user.getId()) && friends2.contains(user.getId()))
                .collect(Collectors.toList());
    }
}
