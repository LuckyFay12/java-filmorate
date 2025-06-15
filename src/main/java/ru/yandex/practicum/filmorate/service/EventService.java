package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;

    public Event addEvent(Event event) {
        return eventStorage.addEvent(event);
    }

    public List<Event> getUserEvents(Long userId) {
        return eventStorage.getUserEvents(userId);
    }
}

