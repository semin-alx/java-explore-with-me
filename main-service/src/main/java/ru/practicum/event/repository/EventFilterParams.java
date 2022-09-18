package ru.practicum.event.repository;

import lombok.Builder;
import lombok.Data;
import ru.practicum.event.type.EventState;
import java.time.LocalDateTime;

/**
 * Данный класс группирует все критерии поиска событий в один объект
 */
@Data
@Builder
public class EventFilterParams {
    private String text;
    private Long[] categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Long[] users;
    private EventState[] states;
}
