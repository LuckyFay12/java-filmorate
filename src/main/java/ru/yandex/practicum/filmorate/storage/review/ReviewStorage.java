package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review getReview(long id);

    List<Review> getReviews(long filmId, int count);

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long id);

    Review likeReview(long id, long userId);

    Review deleteLikeReview(long id, long userId);

    Review dislikeReview(long id, long userId);

    Review deleteDislikeReview(long id, long userId);
}
