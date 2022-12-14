package ru.practicum.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatisticService;
import ru.practicum.statistic.dto.EndpointHit;
import ru.practicum.statistic.dto.ViewStats;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping(value = "/hit")
    public void add(@Valid @RequestBody EndpointHit endpointHit) {
        statisticService.add(endpointHit);
    }

    @GetMapping(value = "/stats")
    public List<ViewStats> getStatistic(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String[] uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statisticService.getStatistic(start, end, uris, unique);
    }

}
