package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmShortInfoDtoMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Primary
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmResultSetExtractor filmResultSetExtractor;

    @Override
    public Film add(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }

        if (film.getGenres() != null) {
            String genreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";

            jdbcTemplate.batchUpdate(genreSql,
                    film.getGenres().stream()
                            .distinct()
                            .map(genre -> new Object[]{id, genre.getId()})
                            .collect(Collectors.toList())
            );
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*, r.mpa_id, r.name AS mpa_name, " +
                "g.genre_id, g.name AS genre_name, " +
                "FROM films f " +
                "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id";
        return jdbcTemplate.query(sql, filmResultSetExtractor);
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new IllegalArgumentException("Id фильма не может быть null");
        }
        if (getById(film.getId()) == null) {
            throw new FilmNotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Film getById(Long id) {
        String sql = "SELECT f.*, r.mpa_id, r.name AS mpa_name, " +
                "g.genre_id, g.name AS genre_name, " +
                "FROM films f " +
                "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                "WHERE f.id = ?";
        List<Film> films = jdbcTemplate.query(sql, filmResultSetExtractor, id);
        return films.stream().findFirst().orElse(null);
    }

    @Override
    public List<Film> getRecommendations(Long userId) {
        String sql = "SELECT f.*, m.name AS mpa_name FROM films f JOIN mpa m ON " +
                "f.mpa_id = m.id WHERE f.film_id in (SELECT film_id FROM likes WHERE user_id in (SELECT user_id FROM likes" +
                " WHERE film_id in (SELECT film_id FROM likes WHERE user_id = ?) and user_id not in (?) GROUP BY user_id " +
                "limit 1)) and f.film_id not in (SELECT film_id FROM likes WHERE user_id = ?)";
        return jdbcTemplate.query(sql, filmResultSetExtractor, userId, userId, userId);
    }
}