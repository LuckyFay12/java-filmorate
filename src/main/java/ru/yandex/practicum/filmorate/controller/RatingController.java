package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public List<RatingMPA> getAllRatings() {
        log.info("Получен HTTP-запрос на список рейтингов");
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public RatingMPA getRatingById(@PathVariable Long id) {
        log.info("Получен запрос на получение рейтинга по id {}", id);
        return ratingService.getRatingById(id);
    }
}
