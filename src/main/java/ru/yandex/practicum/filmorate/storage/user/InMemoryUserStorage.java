package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> idToUser = new HashMap<>();
    private Long idCounter = 1L;

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(idCounter++);
        idToUser.put(user.getId(), user);
        return user;
    }

    public List<User> getAll() {
        return new ArrayList<>(idToUser.values());
    }

    public User getById(Long id) {
        return idToUser.values().stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElseThrow(
                        () -> {
                            String errorMessage = String.format("Пользователь с id %d не найден", id);
                            log.error(errorMessage);
                            throw new UserNotFoundException(errorMessage);
                        });
    }

    public User update(User user) {
        Long id = user.getId();
        if (!idToUser.containsKey(id)) {
            String errorMessage = String.format("Пользователь с id %d не найден", id);
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        idToUser.put(user.getId(), user);
        return user;
    }
}
