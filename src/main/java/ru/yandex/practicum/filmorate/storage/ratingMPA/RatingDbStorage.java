package ru.yandex.practicum.filmorate.storage.ratingMPA;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.mapper.RatingMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RatingDbStorage implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RatingMapper ratingMapper;


    @Override
    public List<RatingMPA> getAllRatings() {
        String sql = "SELECT mpa_id, name AS mpa_name FROM mpa_ratings ORDER BY mpa_id";
        return jdbcTemplate.query(sql, ratingMapper);
    }

    @Override
    public Optional<RatingMPA> getRatingById(Long id) {
        String sql = "SELECT mpa_id, name AS mpa_name FROM mpa_ratings WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, ratingMapper, id).stream().findFirst();
    }
}
