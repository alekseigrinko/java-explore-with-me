package ru.practicum.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.statistic.model.EventStatistic;


public interface EventStatisticRepository extends JpaRepository<EventStatistic, Long> {

}
