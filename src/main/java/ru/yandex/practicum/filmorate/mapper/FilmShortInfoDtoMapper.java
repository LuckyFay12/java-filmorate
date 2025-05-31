package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmShortInfoDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmShortInfoDtoMapper implements RowMapper<FilmShortInfoDto> {

    @Override
    public FilmShortInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmShortInfoDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                .duration(rs.getInt("duration"))
                .build();
    }
}
