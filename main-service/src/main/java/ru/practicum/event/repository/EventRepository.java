package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.type.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryFilterEx {

    List<Event> findEventsByOwnerId(long ownerId, Pageable pageable);

    List<Event> findEx(EventFilterParams filterParams, EventSort sort, Pageable pageable);

    @Query(value = "SELECT MIN(created) FROM events WHERE id in :eventIds", nativeQuery = true)
    LocalDateTime getMinCreatedDate(Long[] eventIds);

}
