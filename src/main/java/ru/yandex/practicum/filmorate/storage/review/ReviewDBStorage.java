package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.mapper.ReviewMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ReviewDBStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    @Override
    public Review getReview(long id) {
        String sql = "SELECT * FROM film_reviews WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, reviewMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFoundException("Отзыв с Id " + id + " не найден");
        }
    }

    @Override
    public List<Review> getReviews(long filmId, int count) {
        String sql = filmId > 0 ?
                "SELECT * FROM film_reviews WHERE film_id = ? ORDER BY useful DESC LIMIT ?" :
                "SELECT * FROM film_reviews ORDER BY useful DESC LIMIT ?";
        return filmId > 0 ?
                jdbcTemplate.query(sql, reviewMapper, filmId, count) :
                jdbcTemplate.query(sql, reviewMapper, count);
    }

    @Override
    public Review createReview(Review review) {
        String sql = "INSERT INTO film_reviews (film_id, user_id, content, is_positive, useful) VALUES (?, ?, ?, ?, 0)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, review.getFilmId());
            ps.setObject(2, review.getUserId());
            ps.setObject(3, review.getContent());
            ps.setObject(4, review.getIsPositive());
            return ps;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        // задаем начальное значение полезности
        review.setUseful(0);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE film_reviews SET content = ?, is_positive = ? WHERE id = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        return getReview(review.getReviewId());
    }

    @Override
    public void deleteReview(long id) {
        String sql = "DELETE FROM film_reviews WHERE id = ?";
        // Удаляем все лайки и дизлайки, связанные с отзывом
        String deleteLikesSql = "DELETE FROM reviews_likes WHERE review_id = ?";
        jdbcTemplate.update(deleteLikesSql, id);
        jdbcTemplate.update(sql, id);
    }

    private void updateUseful(long reviewId) {
        // +1 за лайк, -1 за дизлайк, 0 если нет лайков или дизлайков
        String sql = """
                SELECT
                SUM(
                CASE WHEN is_positive THEN 1
                WHEN NOT is_positive THEN -1
                ELSE 0 END)
                as useful
                FROM reviews_likes
                WHERE review_id = ?
                """;
        Integer useful = jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
        jdbcTemplate.update("UPDATE film_reviews SET useful = ? WHERE id = ?", useful == null ? 0 : useful, reviewId);
    }

    @Override
    public Review likeReview(long id, long userId) {
        String sql = """
                MERGE INTO reviews_likes
                (review_id, user_id, is_positive)
                KEY (review_id, user_id)
                VALUES (?, ?, TRUE)
                """;
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
        return getReview(id);
    }

    @Override
    public Review deleteLikeReview(long id, long userId) {
        String sql = "DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
        return getReview(id);
    }

    public Review dislikeReview(long id, long userId) {
        String sql = """
                MERGE INTO reviews_likes
                (review_id, user_id, is_positive)
                KEY (review_id, user_id)
                VALUES (?, ?, FALSE)
                """;
        jdbcTemplate.update(sql, id, userId);
        updateUseful(id);
        return getReview(id);
    }

    public Review deleteDislikeReview(long id, long userId) {
        // Переиспользуем метод deleteLikeReview, так как логика удаления лайка и дизлайка одинаковая
        return deleteLikeReview(id, userId);
    }
}

