package ru.practicum.statistic;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public interface StatisticService {

    /**
     * Получение статистики для набора событий
     * @param eventIds - список идентификаторов событий, по которым нужно получить число просмотров
     * @return - Map key - идентификатор события, value - число просмотров
     * Кол-во элементов в результирующем Map будет всегда равно числу элементов, переданных в eventIds
     */
    Map<Long, Long> getEventViewCount(Set<Long> eventIds);

    /**
     * Вариант получения числа просмотров для одного события
     */
    long getEventViewCount(long eventId);

    /**
     * Уведомление сервера статистики
     */
    void addStatistic(HttpServletRequest httpRequest);

}
