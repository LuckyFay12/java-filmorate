package ru.yandex.practicum.filmorate.handlers;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.ApiError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFound(UserNotFoundException e) {
        log.debug("Пользователь не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFilmNotFound(FilmNotFoundException e) {
        log.debug("Фильм не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        log.warn("Ошибка валидации: {}", e.getMessage());
        return ApiError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .description(e.getMessage())
                .build();
    }
}
