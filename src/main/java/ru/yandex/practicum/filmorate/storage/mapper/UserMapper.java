package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("id");
        String userName = rs.getString("name");
        String userLogin = rs.getString("login");
        String userEmail = rs.getString("email");
        LocalDate userBirthday = rs.getTimestamp("birthday").toLocalDateTime().toLocalDate();

        return User.builder()
                .id(userId)
                .name(userName)
                .login(userLogin)
                .email(userEmail)
                .birthday(userBirthday)
                .build();
    }

}

