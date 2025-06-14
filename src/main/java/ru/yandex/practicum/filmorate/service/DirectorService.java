package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Director add(Director director) {
        return directorStorage.add(director);
    }

    public Director update(Director director) {
        Long id = director.getId();
        if (id == null) {
            throw new IllegalArgumentException("Id режиссёра не может быть null");
        }
        if (getById(id) == null) {
            String errorMessage = String.format("Режиссёр с id %d не найден", id);
            log.error(errorMessage);
            throw new DirectorNotFoundException(errorMessage);
        }
        return directorStorage.update(director);
    }

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getById(Long id) {
        return directorStorage.getById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёр с id=" + id + " не найден"));
    }

    public void deleteById(Long id) {
        directorStorage.deleteById(id);
    }
}
