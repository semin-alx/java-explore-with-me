package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.helper.StatisticMapper;
import ru.practicum.model.Statistic;
import ru.practicum.repository.StatisticInfo;
import ru.practicum.repository.StatisticRepository;
import ru.practicum.statistic.dto.EndpointHit;
import ru.practicum.statistic.dto.ViewStats;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.practicum.utils.DateTimeUtils.strToDateTime;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public void add(EndpointHit endpointHit) {
        Statistic statistic = StatisticMapper.toStatistic(endpointHit);
        statisticRepository.save(statistic);
    }

    @Override
    public List<ViewStats> getStatistic(String start, String end, String[] uris, boolean unique) {

        LocalDateTime dateStart = strToDateTime(start);
        LocalDateTime dateEnd = strToDateTime(end);

        List<ViewStats> resultList = new ArrayList<>();

        Arrays.stream(uris).forEach(uri -> {
            Optional<ViewStats> stat = getViewStatistic(dateStart, dateEnd, uri, unique);
            stat.ifPresent(resultList::add);
        });

        return resultList;

    }

    private Optional<ViewStats> getViewStatistic(LocalDateTime start, LocalDateTime end,
                                                 String uri, boolean unique) {

        StatisticInfo info = statisticRepository.getStatistic(start, end, uri);

        if (info == null) {
            return Optional.empty();
        }

        long count = unique ? info.getCountUniqIp() : info.getCountAll();

        return Optional.of(new ViewStats(info.getAppName(), uri, count));

    }

}
