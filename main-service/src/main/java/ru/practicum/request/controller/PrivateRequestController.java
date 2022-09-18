package ru.practicum.request.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.service.RequestService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Validated
public class PrivateRequestController {

    private final RequestService requestService;

    public PrivateRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(value = "/{userId}/requests")
    public List<ParticipationRequestDto> find(@PathVariable @Positive long userId) {
        return requestService.find(userId);
    }

    @PostMapping(value = "/{userId}/requests")
    public ParticipationRequestDto create(@PathVariable @Positive long userId,
                                          @RequestParam(required = true) Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable @Positive long userId,
                       @PathVariable @Positive long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping(value = "/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findEventRequests(
            @PathVariable @Positive long userId,
            @PathVariable @Positive long eventId) {
        return requestService.findEventRequests(userId, eventId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approve(@PathVariable @Positive long userId,
                                           @PathVariable @Positive long eventId,
                                           @PathVariable @Positive long reqId) {
        return requestService.approveRequest(userId, eventId, reqId);
    }

    @PatchMapping(value = "/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto reject(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long eventId,
                                          @PathVariable @Positive long reqId) {
        return requestService.rejectRequest(userId, eventId, reqId);
    }


}
