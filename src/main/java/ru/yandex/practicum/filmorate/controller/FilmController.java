package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Получен HTTP-запрос на добавление фильма {}", film);
        Film createdFilm = filmStorage.add(film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма {}", film);
        return createdFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return filmStorage.getAll();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен HTTP-запрос на обновление фильма с id {}", film.getId());
        Film updatedFilm = filmStorage.update(film);
        log.info("Успешно обработан HTTP-запрос на обновление фильма с id {}", film.getId());
        return updatedFilm;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film film = filmStorage.getById(id);
        log.debug("Найденный фильм: {}", film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на добавление лайка к фильму с id {}", id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на удаление лайка к фильму с id {}", id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос на список популярных фильмов, число={}", count);
        return filmService.getPopularFilm(count);
    }
}
