package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    public Event addEvent(Event event) {
        // если timestamp не указан, используем текущее время в миллисекундах
        long timestamp = event.getTimestamp() != null ? event.getTimestamp() : System.currentTimeMillis();
        event.setTimestamp(timestamp);
        log.debug("Добавление события: {}", event);
        return eventStorage.addEvent(event);
    }

    public List<Event> getUserEvents(Long userId) {
        userStorage.getById(userId);
        return eventStorage.getUserEvents(userId);
    }
}

