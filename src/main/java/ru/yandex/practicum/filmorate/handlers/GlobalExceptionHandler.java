package ru.yandex.practicum.filmorate.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ApiError;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

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

    @ExceptionHandler(DirectorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleDirectorNotFound(DirectorNotFoundException e) {
        log.debug("Режиссёр не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleGenreNotFound(GenreNotFoundException e) {
        log.debug("Жанр не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRatingNotFound(RatingNotFoundException e) {
        log.debug("Рейтинг не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFilmNotFound(FilmNotFoundException e) {
        log.debug("Фильм не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Ошибка валидации: {}", message);
        return ApiError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .description(message)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ApiError.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleReviewNotFound(ReviewNotFoundException e) {
        log.debug("Отзыв не найден: {}", e.getMessage(), e);
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(e.getMessage())
                .build();
    }
}
