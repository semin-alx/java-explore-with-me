package ru.practicum.event.helper;

import ru.practicum.category.model.Category;
import ru.practicum.category.helper.CategoryMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.type.EventState;
import ru.practicum.user.helper.UserMapper;
import ru.practicum.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto) {

        boolean paid = false;
        boolean requestModeration = false;
        int participantLimit = 0;

        if (newEventDto.getPaid() != null) {
            paid = newEventDto.getPaid();
        }

        if (newEventDto.getRequestModeration() != null) {
            requestModeration = newEventDto.getRequestModeration();
        }

        if (newEventDto.getParticipantLimit() != null) {
            participantLimit = newEventDto.getParticipantLimit();
        }

        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(new Category(newEventDto.getCategory(), ""))
                .title(newEventDto.getTitle())
                .description(newEventDto.getDescription())
                .created(LocalDateTime.now())
                .eventDate(getDateTimeFromStr(newEventDto.getEventDate(), "eventDate"))
                .locationLat(newEventDto.getLocation().getLat())
                .locationLon(newEventDto.getLocation().getLon())
                .paid(paid)
                .participantLimit(participantLimit)
                .requestModeration(requestModeration)
                .state(EventState.PENDING)
                .build();

    }

    public static void prepareForUpdate(UpdateEventRequest updateEventRequest, Event updateEvent) {
        prepareUpdateEvent((BaseUpdateEvent) updateEventRequest, updateEvent);
    }

    public static void prepareForUpdate(AdminUpdateEventRequest adminUpdateEventRequest,
                                        Event updateEvent) {

        prepareUpdateEvent((BaseUpdateEvent) adminUpdateEventRequest, updateEvent);

        if (adminUpdateEventRequest.getLocation() != null) {
            updateEvent.setLocationLat(adminUpdateEventRequest.getLocation().getLat());
            updateEvent.setLocationLon(adminUpdateEventRequest.getLocation().getLon());
        }

    }

    public static EventFullDto toEventFullDto(Event event) {

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(0L)
                .createdOn(DateTimeUtils.dateTimeToStr(event.getCreated()))
                .description(event.getDescription())
                .eventDate(DateTimeUtils.dateTimeToStr(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getOwner()))
                .location(new Location(event.getLocationLat(), event.getLocationLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(DateTimeUtils.dateTimeToStr(event.getPublished()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(0L)
                .build();

    }

    public static EventShortDto toEventShortDto(Event event) {

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(0L)
                .eventDate(DateTimeUtils.dateTimeToStr(event.getEventDate()))
                .initiator(UserMapper.toUserShortDto(event.getOwner()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(0L)
                .build();

    }

    private static LocalDateTime getDateTimeFromStr(String value, String jsonName) {

        try {
            return DateTimeUtils.strToDateTime(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format("Ошибка формата даты '%s': %s",
                    jsonName,  e.getMessage()));
        }

    }

    private static void prepareUpdateEvent(BaseUpdateEvent baseUpdateEvent, Event updateEvent) {

        if (baseUpdateEvent.getAnnotation() != null) {
            updateEvent.setAnnotation(baseUpdateEvent.getAnnotation());
        }

        if (baseUpdateEvent.getCategory() != null) {
            updateEvent.setCategory(new Category(baseUpdateEvent.getCategory(), ""));
        }

        if (baseUpdateEvent.getTitle() != null) {
            updateEvent.setTitle(baseUpdateEvent.getTitle());
        }

        if (baseUpdateEvent.getDescription() != null) {
            updateEvent.setDescription(baseUpdateEvent.getDescription());
        }

        if (baseUpdateEvent.getEventDate() != null) {
            updateEvent.setEventDate(getDateTimeFromStr(baseUpdateEvent.getEventDate(), "eventDate"));
        }

        if (baseUpdateEvent.getPaid() != null) {
            updateEvent.setPaid(baseUpdateEvent.getPaid());
        }

        if (baseUpdateEvent.getParticipantLimit() != null) {
            updateEvent.setParticipantLimit(baseUpdateEvent.getParticipantLimit());
        }

    }

}
