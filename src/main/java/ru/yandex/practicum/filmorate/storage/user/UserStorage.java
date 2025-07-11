package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    List<User> getAll();

    User getById(Long id);

    User update(User user);

    void deleteById(Long id);
}
