package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmShortInfoDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Получен HTTP-запрос на добавление фильма {}", film);
        Film createdFilm = filmService.add(film);
        log.info("Успешно обработан HTTP-запрос на добавление фильма {}", film);
        return createdFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен HTTP-запрос на получение всех фильмов");
        return filmService.getAll();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен HTTP-запрос на обновление фильма с id {}", film.getId());
        Film updatedFilm = filmService.update(film);
        log.info("Успешно обработан HTTP-запрос на обновление фильма с id {}", film.getId());
        return updatedFilm;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение фильма по id: {}", id);
        Film film = filmService.getById(id);
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
    public List<FilmShortInfoDto> getPopularFilm(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос на список популярных фильмов, число={}", count);
        return filmService.getPopularFilm(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirectorId(@PathVariable Long directorId, @RequestParam(defaultValue = "likes") String sortBy) {
        log.info("Получен HTTP-запрос на получение списка фильмов режиссёра с id {}, сортированных по {}", directorId, sortBy);
        return filmService.getFilmsByDirectorId(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> getFilmsBySearch(@RequestParam String query, @RequestParam(defaultValue = "title") List<String> by) {
        log.info("Поиск фильмов по запросу: {}", query);
        return filmService.getFilmsBySearch(query, by);
    }
}
