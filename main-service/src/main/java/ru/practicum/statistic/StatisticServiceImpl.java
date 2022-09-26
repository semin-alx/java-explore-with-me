package ru.practicum.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.common.error.exception.StatisticServiceUnavailableException;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.statistic.httpclient.HttpStatisticClient;
import ru.practicum.statistic.dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final HttpStatisticClient httpStatisticClient;
    private final EventRepository eventRepository;

    @Override
    public Map<Long, Long> getEventViewCount(Set<Long> eventIds) {

        // Получим дату, когда было создано самое первое событие из запрашиваемых
        // Считывать статистику ранее этой даты смысла нет
        Long[] ids = eventIds.toArray(new Long[eventIds.size()]);
        LocalDateTime minCreateDate = eventRepository.getMinCreatedDate(ids);

        // Мы в параметрах передаем массив идентификаторов событий, по которым хотим получить статистику
        // преобразуем их в uri и заполним в map
        Map<String, Long> uris = eventIds.stream().collect(Collectors.toMap((l -> "/events/" + l.toString()), (l -> l)));

        ResponseEntity<List<ViewStats>> httpAnswer = httpStatisticClient.getStatistic(
                minCreateDate,
                LocalDateTime.now(),
                uris.keySet().toArray(new String[uris.keySet().size()]),
                false);

        if (httpAnswer.getStatusCode() != HttpStatus.OK) {
            throw new StatisticServiceUnavailableException("Сервис статистики вернул код ошибки: "
                    + httpAnswer.getStatusCode());
        }

        List<ViewStats> viewStatsList = httpAnswer.getBody();

        // Мы делаем так, чтобы в результирующем map были все запрашиваемые события, независимо от
        // наличия их в статистики, поэтому заполним map дефолтными нулями
        Map<Long, Long> result = eventIds.stream().collect(Collectors.toMap(l -> l, l -> 0L));

        // Прописываем число просмотров для тех событий, которые нашлись в статистике
        viewStatsList.forEach(vs -> {

            Long eventId = uris.get(vs.getUri());
            Long viewCount = vs.getHits();

            result.put(eventId, viewCount);

        });

        return result;

    }

    @Override
    public long getEventViewCount(long eventId) {
        return getEventViewCount(Set.of(eventId)).get(eventId);
    }

    @Override
    public void addStatistic(HttpServletRequest httpRequest) {
        httpStatisticClient.addStatistic(httpRequest.getRequestURI(), httpRequest.getRemoteAddr());
    }

}
