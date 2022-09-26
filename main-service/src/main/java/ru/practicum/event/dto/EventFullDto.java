package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.event.type.EventState;

/**
 * Наследование EventFullDto от EventShortDto позволит нам писать общие методы
 * для обработки полей, которые присутствуют в обоих dto
 * например, это полезно для обогащения поля views и confirmedRequests
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class EventFullDto extends EventShortDto {

    private String createdOn;

    private String description;

    private Location location;

    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    private EventState state;

}
