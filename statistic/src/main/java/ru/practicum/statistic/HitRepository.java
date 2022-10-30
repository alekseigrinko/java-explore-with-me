package ru.practicum.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistic.model.Hit;

import java.time.LocalDateTime;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("select count (hit) from Hit hit " +
            " where hit.timestamp >= ?1 and hit.timestamp <= ?2 and hit.uri = ?3")
    long getHits(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select distinct count (hit.ip) from Hit hit " +
            " where hit.timestamp >= ?1 and hit.timestamp <= ?2 and hit.uri = ?3")
    long getUniqueHits(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select count (hit) from Hit hit " +
            " where hit.uri = ?1")
    long getViews(String uri);
}
