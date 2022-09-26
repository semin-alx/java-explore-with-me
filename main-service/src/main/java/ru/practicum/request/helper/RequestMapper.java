package ru.practicum.request.helper;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.utils.DateTimeUtils;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(DateTimeUtils.dateTimeToStr(request.getCreated()))
                .event(request.getEventId())
                .requester(request.getUserId())
                .status(request.getStatus())
                .build();
    }

}
