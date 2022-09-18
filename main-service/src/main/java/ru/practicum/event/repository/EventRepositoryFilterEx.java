package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;
import ru.practicum.event.type.EventSort;

import java.util.List;

/**
 * Интерфейс для реализации расширенного поиска событий по набору разных
 * критериев
 */
public interface EventRepositoryFilterEx {

    List<Event> findEx(EventFilterParams filterParams, EventSort sort, Pageable pageable);

}
