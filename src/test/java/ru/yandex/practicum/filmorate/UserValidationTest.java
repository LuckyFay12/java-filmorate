package ru.yandex.practicum.filmorate;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.Validation;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {
    private static Validator validator;

    @BeforeAll
    static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void createValidUser() {
        User user = new User(1L, "testName", "testLogin", "user@mail.ru", LocalDate.of(2000, 12, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test
    void loginShouldNotBeBlank() {
        User user = new User(1L, "testName", "", "user@mail.ru", LocalDate.of(2000, 12, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Логин не должен быть пустым и содержать пробелов", violations.iterator().next().getMessage());
    }

    @Test
    void emailShouldNotBeEmpty() {
        User user = new User(1L, "testName", "testLogin", "", LocalDate.of(2000, 12, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Электронная почта не может быть пустой", violations.iterator().next().getMessage());
    }

    @Test
    void emailMustBeInTheCorrectFormat() {
        User user = new User(1L, "testName", "testLogin", "user.mail.ru", LocalDate.of(2000, 12, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Некорректный формат электронной почты", violations.iterator().next().getMessage());
    }

    @Test
    void futureBirthdayShouldFail() {
        User user = new User(1L, "testName", "testLogin", "user@mail.ru", LocalDate.of(2030, 12, 12));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("День рождение не может быть в будущем", violations.iterator().next().getMessage());
    }
}
