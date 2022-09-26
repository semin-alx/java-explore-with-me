package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.request.type.RequestStatus;

@Data
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {

    private Long id;

    private String created;

    private Long event;

    private Long requester;

    private RequestStatus status;

}
