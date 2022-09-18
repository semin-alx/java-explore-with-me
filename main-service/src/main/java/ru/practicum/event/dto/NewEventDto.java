package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.utils.DateTimeUtils;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@Builder
public class NewEventDto {

    @NotBlank
    private String annotation;

    @NotNull
    @Positive
    private Long category;

    @NotBlank
    private String description;

    @NotNull
    @Pattern(regexp = DateTimeUtils.DATETIME_REGEXP)
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank
    private String title;

}
