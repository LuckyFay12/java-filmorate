package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.dto.FilmShortInfoDto;

import java.util.List;

public interface LikeStorage {
    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<FilmShortInfoDto> getPopularFilm(int count);
}
