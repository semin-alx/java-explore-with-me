package ru.practicum.statistic_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.utils.DateTimeUtils;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder
public class EndpointHit {

    private Long id;

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    @Pattern(regexp = DateTimeUtils.DATETIME_REGEXP)
    private String timestamp;

}
