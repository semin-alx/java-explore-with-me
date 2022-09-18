package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.repository.EventFilterParams;
import ru.practicum.event.type.EventSort;

import java.util.List;

public interface EventService {

    EventFullDto create(long ownerId, NewEventDto newEventDto);

    EventFullDto update(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto update(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    List<EventShortDto> getUserEvents(long userId, Integer from, Integer size);

    EventFullDto getUserEventById(long userId, long eventId);

    EventFullDto cancelUserEvent(long userId, long eventId);

    List<EventShortDto> findEventsAsShort(EventFilterParams filterParams, EventSort sort,
                                          Integer from, Integer size);

    List<EventFullDto> findEventsAsFull(EventFilterParams filterParams, Integer from, Integer size);

    EventFullDto findEventById(Long eventId);

    EventFullDto publish(long eventId);

    EventFullDto reject(long eventId);

}
