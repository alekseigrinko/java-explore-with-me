package ru.practicum.statistic.service;

import ru.practicum.statistic.dto.HitDto;
import ru.practicum.statistic.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс по работе с данными статистики
 * */
public interface HitService {

    /**
     * Метод сохранения данных запроса на сервере статистики
     * @param hitDto - DTO c данными запроса
     * @return возвращает DTO-класс с данными запроса, размещенного на сервер статистики
     * @see HitDto
     * */
    HitDto postHit(HitDto hitDto);

    /**
     * Метод сохранения данных запроса на сервере статистики
     * @param start - дата начала периода, по которому производится сбор статистики
     * @param end - дата окончания периода, по которому производится сбор статистики
     * @param uris - список URI запросов
     * @param unique - признаков поиска запрос по уникальным посещениям (без учета дублей IP-адресов)
     * @return возвращает список DTO с данными статистики
     * @see ViewStatsDto
     * */
    List<ViewStatsDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    /**
     * Метод получения данных с сервера статистики по количеству просмотров события
     * @param uri - URI запроса
     * @return количество просмотров по URI запроса
     * */
    long getViews(String uri);

}
