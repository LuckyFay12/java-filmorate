package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Long, Film> idToFilm = new HashMap<>();
    private Long idCounter = 1L;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен HTTP-запрос на добавление фильма {}", film);
        film.setId(idCounter++);
        idToFilm.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return new ArrayList<>(idToFilm.values());
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Long id = film.getId();
        log.info("Получен HTTP-запрос на обновление фильма с id {}", id);
        if (!idToFilm.containsKey(id)) {
            String errorMessage = String.format("Фильм с id %d не найден", id);
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
        idToFilm.put(film.getId(), film);
        log.info("Успешно обработан HTTP-запрос на обновление пользователя с id {}", id);
        return film;
    }
}
