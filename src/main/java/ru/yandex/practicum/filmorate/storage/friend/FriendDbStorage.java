package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sql,userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f ON f.friend_id = u.id WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, ps -> ps.setLong(1, userId), userMapper);
    }

    @Override
    public List<User> getCommonFriends(Long userId1, Long userId2) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f1 ON u.id = f1.friend_id AND f1.user_id = ? " +
                "JOIN friends f2 ON u.id = f2.friend_id AND f2.user_id = ?";
        return jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, userId1);
            ps.setLong(2, userId2);
        }, userMapper);
    }
}
