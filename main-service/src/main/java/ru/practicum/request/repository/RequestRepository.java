package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import javax.transaction.Transactional;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByUserId(Long userId);

    List<Request> findByEventId(Long eventId);

    @Query(value = "SELECT COUNT(*) FROM requests " +
                   "WHERE status = 'APPROVED' " +
                   "AND event_id = :eventId ",
           nativeQuery = true)
    long getConfirmCount(long eventId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE requests SET status = 'REJECTED'" +
                   "WHERE status = 'PENDING' AND event_id = :eventId",
            nativeQuery = true)
    void rejectPendingRequests(Long eventId);

}
