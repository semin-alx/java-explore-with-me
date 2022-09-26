package ru.practicum.helper;

import ru.practicum.model.Statistic;
import ru.practicum.statistic.dto.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.utils.DateTimeUtils.strToDateTime;

public class StatisticMapper {

    public static Statistic toStatistic(EndpointHit endpointHit) {

        LocalDateTime hitTime = endpointHit.getTimestamp() == null
                ? LocalDateTime.now()
                : strToDateTime(endpointHit.getTimestamp());

        return new Statistic(null,
                             endpointHit.getApp(),
                             endpointHit.getUri(),
                             endpointHit.getIp(),
                             hitTime);
    }

}
