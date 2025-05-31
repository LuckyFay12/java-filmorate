package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMapper implements RowMapper<RatingMPA> {
    public RatingMPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RatingMPA.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}
