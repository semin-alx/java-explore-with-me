package ru.practicum.event.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.repository.EventFilterParams;
import ru.practicum.event.type.EventState;
import ru.practicum.utils.DateTimeUtils;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> findEvents(
            @RequestParam(required = false) Long[] users,
            @RequestParam(required = false) EventState[] states,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {

        EventFilterParams filterParams = EventFilterParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(DateTimeUtils.strIso8601toDateTime(rangeStart))
                .rangeEnd(DateTimeUtils.strIso8601toDateTime(rangeEnd))
                .build();

        return eventService.findEventsAsFull(filterParams, from, size);

    }

    @PutMapping(value = "/{eventId}")
    public EventFullDto update(@PathVariable @Positive long eventId,
                               @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {

        return eventService.update(eventId, adminUpdateEventRequest);

    }

    @PatchMapping(value = "/{eventId}/publish")
    public EventFullDto publish(@PathVariable @Positive long eventId) {
        return eventService.publish(eventId);
    }

    @PatchMapping(value = "/{eventId}/reject")
    public EventFullDto reject(@PathVariable @Positive long eventId) {
        return eventService.reject(eventId);
    }

}
