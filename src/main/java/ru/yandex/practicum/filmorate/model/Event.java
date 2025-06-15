package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private Long eventId;
    private Long userId;
    private Long timestamp; // milliseconds
    private String eventType;
    private String operation;
    private Long entityId; // идентификатор сущности, с которой произошло событие
}