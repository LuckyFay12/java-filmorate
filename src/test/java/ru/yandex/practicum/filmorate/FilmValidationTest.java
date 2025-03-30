package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidationTest {
    private static Validator validator;

    @BeforeAll
    static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testValidFilm() {
        Film film = new Film(1L, "testName", "testDescription", LocalDate.of(2025, 2, 21), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Ошибок валидации не должно быть");
    }

    @Test
    void nameShouldNotBeBlank() {
        Film film = new Film(1L, "", "testDescription", LocalDate.of(2025, 2, 21), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void descriptionShouldBeNoMoreThan200Characters() {
        String longDescription = "a".repeat(201);
        Film film = new Film(1L, "testName", longDescription, LocalDate.of(2025, 2, 21), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Длина описания должна быть не больше 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    void releaseDateBeforeFirstFilmInHistoryShouldFailValidation() {
        Film film = new Film(1L, "testName", "testDescription", LocalDate.of(1895, 12, 27), 60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Дата релиза фильма — не раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    void durationShouldBePositiveNumber() {
        Film film = new Film(1L, "testName", "testDescription", LocalDate.of(2024, 12, 27), -60);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Продолжительность фильма должна быть положительным числом", violations.iterator().next().getMessage());
    }
}