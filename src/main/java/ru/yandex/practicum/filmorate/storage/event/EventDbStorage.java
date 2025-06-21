package ru.yandex.practicum.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.mapper.EventRowMapper;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.PreparedStatement;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper;

    @Override
    public Event addEvent(Event event) {
        String sql = """
                INSERT INTO events (user_id, created_at, event_type, operation, entity_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"event_id"});
            ps.setLong(1, event.getUserId());
            ps.setLong(2, event.getTimestamp());
            ps.setString(3, event.getEventType());
            ps.setString(4, event.getOperation());
            ps.setLong(5, event.getEntityId());
            return ps;
        }, keyHolder);

        long generatedId = requireNonNull(keyHolder.getKey()).longValue();

        return Event.builder()
                .eventId(generatedId)
                .userId(event.getUserId())
                .timestamp(event.getTimestamp())
                .eventType(event.getEventType())
                .operation(event.getOperation())
                .entityId(event.getEntityId())
                .build();
    }

    @Override
    public List<Event> getUserEvents(Long userId) {
        String sql = "SELECT * FROM events WHERE user_id = ? ORDER BY created_at";
        return jdbcTemplate.query(sql, eventRowMapper, userId);
    }
}
