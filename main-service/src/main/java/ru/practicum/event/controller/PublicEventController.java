package ru.practicum.event.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.repository.EventFilterParams;
import ru.practicum.event.type.EventSort;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.statistic.StatisticService;
import ru.practicum.utils.DateTimeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class PublicEventController {

    private final EventService eventService;
    private final StatisticService statisticService;

    public PublicEventController(EventService eventService, StatisticService statisticService) {
        this.eventService = eventService;
        this.statisticService = statisticService;
    }

    @GetMapping
    public List<EventShortDto> findEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {

        statisticService.addStatistic(request);

        EventFilterParams filterParams = EventFilterParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(DateTimeUtils.strToDateTime(rangeStart))
                .rangeEnd(DateTimeUtils.strToDateTime(rangeEnd))
                .onlyAvailable(onlyAvailable)
                .build();

        return eventService.findEventsAsShort(filterParams, sort, from, size);

    }

    @GetMapping(value = "/{eventId}")
    EventFullDto findEventById(@PathVariable @Positive long eventId,
                               HttpServletRequest request) {
        statisticService.addStatistic(request);
        return eventService.findEventById(eventId);
    }

}
