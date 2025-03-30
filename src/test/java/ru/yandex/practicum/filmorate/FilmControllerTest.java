package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();
    }

    @Test
    void addFilmTest() {
        Film film = new Film(1L, "testName", "testDescription", LocalDate.of(2025, 2, 21), 60);
        filmController.addFilm(film);

        assertEquals(film.toString(), "Film(id=1, name=testName, description=testDescription, releaseDate=2025-02-21, duration=60)");
    }

    @Test
    void updateFilmTest() {
        Film film = new Film(1L, "testName", "testDescription", LocalDate.of(2025, 2, 21), 60);
        filmController.addFilm(film);
        film.setName("Name1");
        filmController.updateFilm(film);

        assertEquals("Name1", film.getName(), "Название должно обновиться");
    }

    @Test
    void getAllFilmsTest() {
        Film film1 = new Film(1L, "testName", "testDescription", LocalDate.of(2025, 2, 21), 60);
        Film film2 = new Film(1L, "testName1", "testDescription1", LocalDate.of(2025, 12, 21), 60);
        filmController.addFilm(film1);
        filmController.addFilm(film2);
        List<Film> films = filmController.getAllFilms();

        assertEquals(2, films.size(), "Фильмов в списке должно быть 2");
    }
}
