package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public Film add(Film film) {
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        return film;
    }

    public List<Film> getAll() {
        return new ArrayList<>(idToFilm.values());
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        if (!idToFilm.containsKey(id)) {
            String errorMessage = String.format("Фильм с id %d не найден", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
        idToFilm.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Long id) {
        return idToFilm.values().stream().filter(film -> Objects.equals(film.getId(), id)).findFirst().orElseThrow(
                () -> {
                    String errorMessage = String.format("Фильм с id %d не найден", id);
                    log.error(errorMessage);
                    throw new FilmNotFoundException(errorMessage);
                });
    }

    @Override  // затычка
    public List<Film> getRecommendations(Long userId) {
        return List.of();
    }
}