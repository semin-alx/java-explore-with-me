package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Statistic;

import java.time.LocalDateTime;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query(value = "SELECT app AS appName, " +
                   "COUNT(*) AS countAll, " +
                   "COUNT(DISTINCT ip) AS countUniqIp " +
                   "FROM statistics " +
                   "WHERE stat_time >= :start AND stat_time <= :end AND uri = :uri " +
                   "GROUP BY app",
           nativeQuery = true)
    StatisticInfo getStatistic(LocalDateTime start, LocalDateTime end, String uri);

}
