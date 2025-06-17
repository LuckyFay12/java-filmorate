package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
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
        if (film.getDirectors() != null) {
            String directorSql = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?);";
            jdbcTemplate.batchUpdate(directorSql,
                    film.getDirectors().stream()
                            .distinct()
                            .map(director -> new Object[]{id, director.getId()})
                            .collect(Collectors.toList())
            );
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*, r.mpa_id, r.name AS mpa_name, " +
                "g.genre_id, g.name AS genre_name, " +
                "d.id AS director_id, d.name AS director_name " +
                "FROM films f " +
                "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                "LEFT JOIN film_directors fd ON fd.film_id = f.id " +
                "LEFT JOIN directors d ON d.id = fd.director_id";
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
        String deleteGenresSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String insertGenresSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(insertGenresSql,
                    film.getGenres().stream()
                            .distinct()
                            .map(genre -> new Object[]{film.getId(), genre.getId()})
                            .collect(Collectors.toList()));
        }

        String deleteDirectorsSql = "DELETE FROM film_directors WHERE film_id = ?";
        jdbcTemplate.update(deleteDirectorsSql, film.getId());


        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            String insertDirectorsSql = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(insertDirectorsSql,
                    film.getDirectors().stream()
                            .distinct()
                            .map(director -> new Object[]{film.getId(), director.getId()})
                            .collect(Collectors.toList()));
        }
        return film;
    }

    @Override
    public Film getById(Long id) {
        String sql = "SELECT f.*, r.mpa_id, r.name AS mpa_name, " +
                "g.genre_id, g.name AS genre_name, " +
                "d.id AS director_id, d.name AS director_name " +
                "FROM films f " +
                "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                "LEFT JOIN film_directors fd ON fd.film_id = f.id " +
                "LEFT JOIN directors d ON d.id = fd.director_id " +
                "WHERE f.id = ?";
        List<Film> films = jdbcTemplate.query(sql, filmResultSetExtractor, id);
        assert films != null;
        return films.stream()
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException("Фильм с id=" + id + " не найден"));
    }


    @Override
    public List<Film> getFilmsByDirectorId(Long directorId, String sortBy) {
        String sql;
        if (sortBy.equals("year")) {
            sql = "SELECT f.*, r.name AS mpa_name, " +
                    "g.genre_id, g.name AS genre_name, " +
                    "d.id AS director_id, d.name AS director_name " +
                    "FROM films f " +
                    "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                    "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                    "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                    "JOIN film_directors fd ON fd.film_id = f.id " +
                    "JOIN directors d ON d.id = fd.director_id " +
                    "WHERE fd.director_id = ? " +
                    "ORDER BY f.release_date";
        } else {
            sql = "SELECT f.*, r.name AS mpa_name, " +
                    "g.genre_id, g.name AS genre_name, " +
                    "d.id AS director_id, d.name AS director_name, " +
                    "COUNT(l.user_id) AS likes_count " +
                    "FROM films f " +
                    "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                    "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                    "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                    "JOIN film_directors fd ON fd.film_id = f.id " +
                    "JOIN directors d ON d.id = fd.director_id " +
                    "LEFT JOIN likes l ON f.id = l.film_id " +
                    "WHERE fd.director_id = ? " +
                    "GROUP BY f.id, r.mpa_id, g.genre_id, d.id " +
                    "ORDER BY likes_count DESC";
        }
        List<Film> films = jdbcTemplate.query(sql, filmResultSetExtractor, directorId);
        return films != null ? films : Collections.emptyList();
    }

    @Override
    public List<Film> getFilmsByParam(String queryLowerCase, boolean searchByTitle, boolean searchByDirector) {
        List<Object> params = new ArrayList<>();
        if (searchByTitle) {
            params.add(queryLowerCase);
        }
        if (searchByDirector) {
            params.add(queryLowerCase);
        }
        String sql = """
                SELECT f.*,
                r.name AS mpa_name,
                g.genre_id, g.name AS genre_name,
                d.id AS director_id, d.name AS director_name,
                COUNT(l.user_id) AS likes_count
                FROM films f
                LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id
                LEFT JOIN film_genres fg ON fg.film_id = f.id
                LEFT JOIN genres g ON g.genre_id = fg.genre_id
                LEFT JOIN film_directors fd ON fd.film_id = f.id
                LEFT JOIN directors d ON d.id = fd.director_id
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE %s
                GROUP BY f.id, r.mpa_id
                ORDER BY likes_count DESC
                """.formatted(buildWhere(searchByTitle, searchByDirector));

        return jdbcTemplate.query(sql, filmResultSetExtractor, params.toArray());
    }

    private String buildWhere(boolean searchByTitle, boolean searchByDirector) {
        List<String> conditions = new ArrayList<>();
        if (searchByTitle) {
            conditions.add("LOWER(f.name) LIKE ?");
        }
        if (searchByDirector) {
            conditions.add("LOWER(d.name) LIKE ?");
        }
        return String.join(" OR ", conditions);
    }

    @Override
    public List<Film> getPopularFilms(int count, Long genreId, Integer year) {
        String sql = """
                SELECT f.*,
                r.name AS mpa_name,
                g.genre_id, g.name AS genre_name,
                d.id AS director_id, d.name AS director_name,
                COUNT(l.user_id) AS likes_count
                FROM films f
                LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id
                LEFT JOIN film_genres fg ON fg.film_id = f.id
                LEFT JOIN genres g ON g.genre_id = fg.genre_id
                LEFT JOIN film_directors fd ON fd.film_id = f.id
                LEFT JOIN directors d ON d.id = fd.director_id
                LEFT JOIN likes l ON f.id = l.film_id
                WHERE g.genre_id = COALESCE(?, g.genre_id)
                  AND EXTRACT(YEAR FROM release_date) = COALESCE(?, EXTRACT(YEAR FROM release_date))
                GROUP BY f.id, r.mpa_id, g.genre_id
                ORDER BY likes_count DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, filmResultSetExtractor, genreId, year, count);
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