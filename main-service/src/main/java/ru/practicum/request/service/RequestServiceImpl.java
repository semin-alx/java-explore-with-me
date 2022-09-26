package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.error.exception.IncorrectActionException;
import ru.practicum.common.error.exception.ObjectNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.type.EventState;
import ru.practicum.request.helper.RequestMapper;
import ru.practicum.request.type.RequestStatus;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(long userId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Указанное событие не найдено"));

        if (event.getOwner().getId().equals(userId)) {
            throw new IncorrectActionException("инициатор события не может добавить запрос " +
                                               "на участие в своём событии");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new IncorrectActionException("нельзя участвовать в неопубликованном событии");
        }

        checkRequestLimit(event);

        Request newRequest = Request.builder()
                .userId(userId)
                .eventId(eventId)
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();

        if (!event.getRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(newRequest));

    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {

        Request request = getAndCheckRequest(requestId);

        if (!request.getUserId().equals(userId)) {
            throw new IncorrectActionException("Нельзя отменить чужую заявку");
        }

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));

    }

    @Override
    public List<ParticipationRequestDto> find(long userId) {
        return requestRepository.findByUserId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> findEventRequests(long userId, long eventId) {

        getAndCheckEvent(userId, eventId);

        return requestRepository.findByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

    }

    @Override
    public ParticipationRequestDto approveRequest(long userId, long eventId, long reqId) {

        Event event = getAndCheckEvent(userId, eventId);
        Request request = getAndCheckRequest(reqId);

        if (!request.getEventId().equals(eventId)) {
            throw new IncorrectActionException("Указанный запрос не соответствует " +
                                               "указанному событию");
        }

        checkRequestLimit(event);

        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);

        // если при подтверждении данной заявки, лимит заявок для события
        // исчерпан, то все неподтверждённые заявки необходимо отклонить
        if (event.getParticipantLimit() > 0) {
            long reqLimit = event.getParticipantLimit();
            long used = requestRepository.getConfirmCount(eventId);
            if (used >= reqLimit) {
                requestRepository.rejectPendingRequests(eventId);
            }
        }

        return RequestMapper.toParticipationRequestDto(request);

    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {

        Event event = getAndCheckEvent(userId, eventId);
        Request request = getAndCheckRequest(reqId);

        if (!request.getEventId().equals(eventId)) {
            throw new IncorrectActionException("Указанный запрос не соответствует " +
                    "указанному событию");
        }

        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);

        return RequestMapper.toParticipationRequestDto(request);
    }

    private Event getAndCheckEvent(long ownerId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Указанное событие не найдено"));

        if (!event.getOwner().getId().equals(ownerId)) {
            throw new IncorrectActionException("Доступ к чужому событию запрещен");
        }

        return event;

    }

    private void checkRequestLimit(Event event) {
        if (event.getParticipantLimit() > 0) {
            if (event.getParticipantLimit() <= requestRepository.getConfirmCount(event.getId())) {
                throw new IncorrectActionException("Достигнут лимит запросов на участие");
            }
        }
    }

    private Request getAndCheckRequest(Long requestId) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Указанная заявка не найдена"));

        return request;

    }

}
