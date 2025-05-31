package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.ratingMPA.RatingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public List<RatingMPA> getAllRatings() {
        return ratingStorage.getAllRatings();
    }

    public RatingMPA getRatingById(Long id) {
        return ratingStorage.getRatingById(id)
                .orElseThrow(() -> new RatingNotFoundException("Рейтинг с id=" + id + " не найден"));
    }
}
