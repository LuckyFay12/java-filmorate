package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmShortInfoDto;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.ratingMPA.RatingStorage;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final LikeStorage likeStorage;
    private final DirectorStorage directorStorage;

    public Film add(Film film) {
        ratingStorage.getRatingById(film.getMpa().getId())
                .orElseThrow(() -> new RatingNotFoundException("MPA с ID " + film.getMpa().getId() + " не найден"));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.getGenreById(genre.getId())
                        .orElseThrow(() -> new GenreNotFoundException("Жанр с ID " + genre.getId() + " не найден"));
            }
        }
        return filmStorage.add(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public void addLike(Long filmId, Long userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    public List<FilmShortInfoDto> getPopularFilm(int count) {
        return likeStorage.getPopularFilm(count);
    }

    public List<Film> getFilmsByDirectorId(Long directorId, String sortBy) {
        directorStorage.getById(directorId)
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёр с id " + directorId + " не найден"));
        if (!sortBy.equals("year") && !sortBy.equals("likes")) {
            throw new ValidationException("SortBy должен быть year или likes");
        }
        return filmStorage.getFilmsByDirectorId(directorId, sortBy);
    }

    public List<Film> getFilmsByParam(String query, List<String> by) {
        Set<String> validBy = Set.of("title", "director");
        if (by.stream().anyMatch(element -> !validBy.contains(element))) {
            throw new IllegalArgumentException("Некорректные параметры запроса, должно быть: title и(или) director");
        }
        String queryLowerCase = "%" + query.toLowerCase() + "%";
        boolean searchByTitle = by.contains("title");
        boolean searchByDirector = by.contains("director");
        return filmStorage.getFilmsByParam(queryLowerCase, searchByTitle, searchByDirector);
    }
}
