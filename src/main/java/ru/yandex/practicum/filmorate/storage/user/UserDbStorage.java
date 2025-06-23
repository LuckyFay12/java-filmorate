package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public List<User> getAll() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, userMapper);
    }

    public User create(User user) {
        String query = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
        return user;
    }

    public User getById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(query, userMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с Id " + id + " не найден");
        }
    }

    public User update(User user) {
        String query = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(query, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteById(Long id) {
        // Clean up all user-related data before deleting the user
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? OR friend_id = ?", id, id);
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ?", id);
        jdbcTemplate.update("DELETE FROM film_reviews WHERE user_id = ?", id);
        jdbcTemplate.update("DELETE FROM reviews_likes WHERE user_id = ?", id);
        String query = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(query, id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }
}
