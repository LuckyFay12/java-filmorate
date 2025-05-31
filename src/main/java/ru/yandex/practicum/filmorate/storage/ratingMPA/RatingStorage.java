package ru.yandex.practicum.filmorate.storage.ratingMPA;

import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {

    List<RatingMPA> getAllRatings();

    Optional<RatingMPA> getRatingById(Long id);
}
