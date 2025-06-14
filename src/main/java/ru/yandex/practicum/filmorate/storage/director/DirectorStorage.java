package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    Director add(Director director);

    Director update(Director director);

    List<Director> getAll();

    Optional<Director> getById(Long id);

    void deleteById(Long id);
}
