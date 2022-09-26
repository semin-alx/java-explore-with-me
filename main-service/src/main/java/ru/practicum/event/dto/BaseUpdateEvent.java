package ru.practicum.event.dto;

import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


/**
 * Базовый класс для AdminUpdateEventRequest и UpdateEventRequest
 * Выделение базового dto для AdminUpdateEventRequest и UpdateEventRequest
 * позволит сократить код при обращении к общим полям в EventMapper
 */
@Data
public class BaseUpdateEvent {

    private String annotation;

    @Positive
    private Long category;

    private String description;

    private String eventDate;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private String title;

}
