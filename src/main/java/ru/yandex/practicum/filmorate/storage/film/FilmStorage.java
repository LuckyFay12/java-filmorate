package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    List<Film> getAll();

    Film update(Film film);

    Film getById(Long id);

    List<Film> getFilmsByDirectorId(Long directorId, String sortedBy);

    List<Film> getFilmsByParam(String queryLowerCase, boolean searchByTitle, boolean searchByDirector);

    List<Film> getPopularFilms(int count, Long genreId, Integer year);

    void deleteById(Long id);
}
