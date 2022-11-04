package ru.practicum.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistic.model.Hit;

import java.time.LocalDateTime;

/**
 * Интерфейс для работы с репозиторием данных статистики
 *
 * @see Hit
 */
public interface HitRepository extends JpaRepository<Hit, Long> {

    /**
     * Метод получения количества записей на сервере статистики по запросу параметров
     * @param start - дата начала периода, по которому производится сбор статистики
     * @param end   - дата окончания периода, по которому производится сбор статистики
     * @param uri  - URI запроса
     * @return количество записей статистики по запросу без учета уникальных пользователей
     */
    @Query("select count (hit) from Hit hit " +
            " where hit.timestamp >= ?1 and hit.timestamp <= ?2 and hit.uri = ?3")
    long getHits(LocalDateTime start, LocalDateTime end, String uri);

    /**
     * Метод получения количества уникальных записей на сервере статистики по запросу параметров
     * @param start - дата начала периода, по которому производится сбор статистики
     * @param end   - дата окончания периода, по которому производится сбор статистики
     * @param uri  - URI запроса
     * @return количество записей статистики по запросу с учетом уникальных пользователей
     */
    @Query("select distinct count (hit.ip) from Hit hit " +
            " where hit.timestamp >= ?1 and hit.timestamp <= ?2 and hit.uri = ?3")
    long getUniqueHits(LocalDateTime start, LocalDateTime end, String uri);

    /**
     * Метод получения записей по URI
     * @param uri  - URI запроса
     * @return количество записей на сервере статистики по URI запроса
     */
    @Query("select count (hit) from Hit hit " +
            " where hit.uri = ?1")
    long getViews(String uri);
}
