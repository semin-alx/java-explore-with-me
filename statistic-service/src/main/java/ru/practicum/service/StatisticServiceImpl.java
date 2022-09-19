package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.helper.StatisticMapper;
import ru.practicum.model.Statistic;
import ru.practicum.repository.StatisticInfo;
import ru.practicum.repository.StatisticRepository;
import ru.practicum.statistic_dto.EndpointHit;
import ru.practicum.statistic_dto.ViewStats;
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

        LocalDateTime date1 = strToDateTime(start);
        LocalDateTime date2 = strToDateTime(end);

        List<ViewStats> resultList = new ArrayList<>();

        Arrays.stream(uris).forEach(uri -> {
            Optional<ViewStats> stat = getViewStatistic(date1, date2, uri, unique);
            if (stat.isPresent()) {
                resultList.add(stat.get());
            }
        });

        return resultList;

    }

    private Optional<ViewStats> getViewStatistic(LocalDateTime date1, LocalDateTime date2,
                                                 String uri, boolean unique) {

        StatisticInfo si = statisticRepository.getStatistic(date1, date2, uri);

        if (si == null) {
            return Optional.empty();
        } else {

            long count;

            if (unique) {
                count = si.getCountUniqIp();
            } else {
                count = si.getCountAll();
            }

            return Optional.of(new ViewStats(si.getAppName(), uri, count));
        }

    }

}
