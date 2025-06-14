package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dto.FilmShortInfoDto;

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
        FilmShortInfoDto film = new FilmShortInfoDto(1L, "testName", "testDescription", LocalDate.of(2025, 2, 21), 60);
        Set<ConstraintViolation<FilmShortInfoDto>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Ошибок валидации не должно быть");
    }

    @Test
    void nameShouldNotBeBlank() {
        FilmShortInfoDto film = new FilmShortInfoDto(1L, "", "testDescription", LocalDate.of(2025, 2, 21), 60);
        Set<ConstraintViolation<FilmShortInfoDto>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void descriptionShouldBeNoMoreThan200Characters() {
        String longDescription = "a".repeat(201);
        FilmShortInfoDto film = new FilmShortInfoDto(1L, "testName", longDescription, LocalDate.of(2025, 2, 21), 60);
        Set<ConstraintViolation<FilmShortInfoDto>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Длина описания должна быть не больше 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    void releaseDateBeforeFirstFilmInHistoryShouldFailValidation() {
        FilmShortInfoDto film = new FilmShortInfoDto(1L, "testName", "testDescription", LocalDate.of(1895, 12, 27), 60);
        Set<ConstraintViolation<FilmShortInfoDto>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Дата релиза фильма — не раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    void durationShouldBePositiveNumber() {
        FilmShortInfoDto film = new FilmShortInfoDto(1L, "testName", "testDescription", LocalDate.of(2024, 12, 27), -60);
        Set<ConstraintViolation<FilmShortInfoDto>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Продолжительность фильма должна быть положительным числом", violations.iterator().next().getMessage());
    }
}