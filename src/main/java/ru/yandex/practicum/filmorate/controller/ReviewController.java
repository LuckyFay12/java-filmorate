package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        log.info("Получен HTTP-запрос на создание отзыва: {}", review);
        Review created = reviewService.create(review);
        log.info("Успешно создан отзыв: {}", created);
        return created;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Получен HTTP-запрос на обновление отзыва: {}", review);
        Review updated = reviewService.update(review);
        log.info("Успешно обновлен отзыв: {}", updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на удаление отзыва с id {}", id);
        reviewService.delete(id);
        log.info("Успешно удален отзыв с id {}", id);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Long id) {
        log.info("Получен HTTP-запрос на получение отзыва по id: {}", id);
        Review review = reviewService.getById(id);
        log.debug("Найденный отзыв: {}", review);
        return review;
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(required = false) Long filmId,
                                   @RequestParam(required = false) Integer count) {
        log.info("Получен HTTP-запрос на получение отзывов по filmId={} и count={}", filmId, count);
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review like(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на лайк отзыва id={} пользователем id={}", id, userId);
        return reviewService.likeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review dislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на дизлайк отзыва id={} пользователем id={}", id, userId);
        return reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на удаление лайка отзыва id={} пользователем id={}", id, userId);
        return reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review deleteDislike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен HTTP-запрос на удаление дизлайка отзыва id={} пользователем id={}", id, userId);
        return reviewService.deleteDislikeReview(id, userId);
    }
}

