package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    @Qualifier("reviewDBStorage")
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final EventService eventService;

    private static final String REVIEW_ID_NULL_MESSAGE = "reviewId не может быть null";
    private static final String FILM_ID_NULL_MESSAGE = "filmId не может быть null";
    private static final String USER_ID_NULL_MESSAGE = "userId не может быть null";

    public Review create(Review review) {
        validateNotNull(review.getFilmId(), FILM_ID_NULL_MESSAGE);
        validateNotNull(review.getUserId(), USER_ID_NULL_MESSAGE);

        userStorage.getById(review.getUserId());
        filmStorage.getById(review.getFilmId());
        Review created = reviewStorage.createReview(review);
        eventService.addEvent(Event.builder()
                .userId(review.getUserId())
                .eventType("REVIEW")
                .operation("ADD")
                .entityId(created.getReviewId())
                .build());
        return created;
    }

    public Review update(Review review) {
        validateNotNull(review.getReviewId(), REVIEW_ID_NULL_MESSAGE);
        reviewStorage.getReview(review.getReviewId());
        Review updated = reviewStorage.updateReview(review);
        eventService.addEvent(Event.builder()
                .userId(updated.getUserId())
                .eventType("REVIEW")
                .operation("UPDATE")
                .entityId(updated.getReviewId())
                .build());
        return updated;
    }

    public void delete(Long id) {
        validateNotNull(id, REVIEW_ID_NULL_MESSAGE);
        Review review = reviewStorage.getReview(id);
        reviewStorage.deleteReview(id);
        eventService.addEvent(Event.builder()
                .userId(review.getUserId())
                .eventType("REVIEW")
                .operation("REMOVE")
                .entityId(id)
                .build());
    }

    public Review getById(Long id) {
        validateNotNull(id, REVIEW_ID_NULL_MESSAGE);
        return reviewStorage.getReview(id);
    }

    public List<Review> getReviews(Long filmId, Integer count) {
        long film = filmId == null ? 0 : filmId;
        int limit = count == null ? 10 : count;
        return reviewStorage.getReviews(film, limit);
    }

    public Review likeReview(Long reviewId, Long userId) {
        validateNotNull(userId, USER_ID_NULL_MESSAGE);
        validateNotNull(reviewId, REVIEW_ID_NULL_MESSAGE);
        reviewStorage.getReview(reviewId);
        return reviewStorage.likeReview(reviewId, userId);
    }

    public Review dislikeReview(Long reviewId, Long userId) {
        validateNotNull(userId, USER_ID_NULL_MESSAGE);
        validateNotNull(reviewId, REVIEW_ID_NULL_MESSAGE);
        reviewStorage.getReview(reviewId);
        return reviewStorage.dislikeReview(reviewId, userId);
    }

    public Review deleteLikeReview(Long reviewId, Long userId) {
        validateNotNull(reviewId, REVIEW_ID_NULL_MESSAGE);
        validateNotNull(userId, USER_ID_NULL_MESSAGE);
        reviewStorage.getReview(reviewId);
        return reviewStorage.deleteLikeReview(reviewId, userId);
    }

    public Review deleteDislikeReview(Long reviewId, Long userId) {
        validateNotNull(reviewId, REVIEW_ID_NULL_MESSAGE);
        validateNotNull(userId, USER_ID_NULL_MESSAGE);
        reviewStorage.getReview(reviewId);
        return reviewStorage.deleteDislikeReview(reviewId, userId);
    }

    private void validateNotNull(Long value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}