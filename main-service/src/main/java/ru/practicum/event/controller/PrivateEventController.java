package ru.practicum.event.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Validated
public class PrivateEventController {

    private final EventService eventService;

    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(value = "/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable @Positive long userId,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero
                                             Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive
                                             Integer size) {

        return eventService.getUserEvents(userId, from, size);

    }

    @PatchMapping(value = "/{userId}/events")
    public EventFullDto update(@PathVariable @Positive long userId,
                               @Valid @RequestBody UpdateEventRequest updateEventRequest) {

        return eventService.update(userId, updateEventRequest);

    }

    @PostMapping(value = "/{userId}/events")
    public EventFullDto create(@PathVariable @Positive long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.create(userId, newEventDto);
    }

    @GetMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable @Positive long userId,
                                         @PathVariable @Positive long eventId) {
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}")
    public EventFullDto cancelUserEvent(@PathVariable @Positive long userId,
                                @PathVariable @Positive long eventId) {
        return eventService.cancelUserEvent(userId, eventId);
    }

}
