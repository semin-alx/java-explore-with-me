package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryReposirory;
import ru.practicum.common.error.exception.IncorrectActionException;
import ru.practicum.common.error.exception.ObjectNotFoundException;
import ru.practicum.event.dto.*;
import ru.practicum.event.helper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventFilterParams;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.type.EventSort;
import ru.practicum.event.type.EventState;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.statistic.StatisticService;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryReposirory categoryReposirory;
    private final RequestRepository requestRepository;
    private final StatisticService statisticService;

    @Override
    public EventFullDto create(long ownerId, NewEventDto newEventDto) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Указанный пользователь в базе не найден"));

        Category category = categoryReposirory.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Указанная категория в базе не найдена"));

        Event newEvent = EventMapper.toEvent(newEventDto);

        newEvent.setOwner(owner);
        newEvent.setCategory(category);

        newEvent = eventRepository.save(newEvent);

        // EventFullDto.views и EventFullDto.confirmedRequests не получаем, событие новое,
        // понятно, что всегда будет 0
        return EventMapper.toEventFullDto(newEvent);

    }

    @Override
    public List<EventShortDto> getUserEvents(long userId, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        List<EventShortDto> listDto = eventRepository.findEventsByOwnerId(userId, pageable).stream()
                                        .map(EventMapper::toEventShortDto)
                                        .collect(Collectors.toList());

        enrichDtoList(listDto);

        return listDto;

    }

    @Override
    public EventFullDto update(long userId, UpdateEventRequest updateEventRequest) {

        Event sourceEvent = getEventAndCheckOwner(userId, updateEventRequest.getEventId());

        if ((sourceEvent.getState() != EventState.PENDING)
                && (sourceEvent.getState() != EventState.CANCELED)) {
            throw new IncorrectActionException("Изменить можно только отмененные события или " +
                                               "события в состоянии ожидания модерации");
        }

        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime newDateTime = DateTimeUtils.strToDateTime(updateEventRequest.getEventDate());
            if (newDateTime.minusHours(2).isBefore(LocalDateTime.now())) {
                throw new IncorrectActionException("Дата и время на которые намечено событие не " +
                                                   "может быть раньше, чем через два часа от " +
                                                   "текущего момента");
            }
        }

        EventMapper.prepareForUpdate(updateEventRequest, sourceEvent);

        Event event = eventRepository.save(sourceEvent);
        return getFullDto(event);

    }

    @Override
    public EventFullDto update(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {

        // Здесь мы по тз не делаем валидацию
        // Предполагаем, что все входные данные верные

        Event sourceEvent = eventRepository.findById(eventId).get();

        EventMapper.prepareForUpdate(adminUpdateEventRequest, sourceEvent);

        Event event = eventRepository.save(sourceEvent);
        return getFullDto(event);

    }

    @Override
    public EventFullDto getUserEventById(long userId, long eventId) {
        Event event = getEventAndCheckOwner(userId, eventId);
        return getFullDto(event);
    }

    @Override
    public EventFullDto cancelUserEvent(long userId, long eventId) {

        Event event = getEventAndCheckOwner(userId, eventId);

        if (event.getState() != EventState.PENDING) {
            throw new IncorrectActionException("Отменить можно только событие в состоянии " +
                                               "ожидания модерации");
        }

        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return getFullDto(event);

    }

    @Override
    public List<EventShortDto> findEventsAsShort(EventFilterParams filterParams, EventSort sort,
                                                 Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> list = eventRepository.findEx(filterParams, sort, pageable);

        List<EventShortDto> listDto = list.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        enrichDtoList(listDto);

        if (sort == EventSort.VIEWS) {
            return listDto.stream()
                    .sorted((o1, o2) -> (int)(o1.getViews() - o2.getViews()))
                    .collect(Collectors.toList());
        } else {
            return listDto;
        }

    }

    @Override
    public EventFullDto findEventById(Long eventId) {
        Event event = getAndCheckEvent(eventId);
        return getFullDto(event);
    }

    @Override
    public List<EventFullDto> findEventsAsFull(EventFilterParams filterParams, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> list = eventRepository.findEx(filterParams, EventSort.EVENT_DATE, pageable);

        List<EventFullDto> listDto = list.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        enrichDtoList(listDto);

        return listDto;

    }

    @Override
    public EventFullDto publish(long eventId) {

        Event event = getAndCheckEvent(eventId);
        LocalDateTime published = LocalDateTime.now();

        if (event.getEventDate().minusHours(1).isBefore(published)) {
            throw new IncorrectActionException("Дата начала события должна быть не ранее " +
                                               "чем за час от даты публикации");
        }

        if (event.getState() != EventState.PENDING) {
            throw new IncorrectActionException("Событие должно быть в состоянии " +
                                               "ожидания публикации");
        }

        event.setPublished(published);
        event.setState(EventState.PUBLISHED);

        eventRepository.save(event);

        return getFullDto(event);

    }

    @Override
    public EventFullDto reject(long eventId) {

        Event event = getAndCheckEvent(eventId);

        if (event.getState() == EventState.PUBLISHED) {
            throw new IncorrectActionException("Событие не должно быть опубликовано");
        }

        event.setState(EventState.CANCELED);

        eventRepository.save(event);

        return getFullDto(event);

    }

    @Override
    public <T extends EventShortDto> void enrichDtoList(List<T> listDto) {

        Set<Long> eventIds = listDto.stream().map(dto -> dto.getId()).collect(Collectors.toSet());
        Map<Long, Long> stat = statisticService.getEventViewCount(eventIds);

        listDto.forEach(dto -> {
            dto.setViews(stat.get(dto.getId()));
            dto.setConfirmedRequests(requestRepository.getConfirmCount(dto.getId()));
        });

    }

    private Event getAndCheckEvent(long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Указанное событие в базе данных не найдено"));

        return event;

    }

    private Event getEventAndCheckOwner(long ownerId, long eventId) {

        Event event = getAndCheckEvent(eventId);

        if (!event.getOwner().getId().equals(ownerId)) {
            throw new IncorrectActionException("Доступ/изменение информации чужого " +
                    "события запрещен");
        }

        return event;

    }

    /**
     * Получает с помощью EventMapper EventFullDto из Event
     * и обогащает числом просмотров из статистики и числом подтвержденных запросов
     */
    private EventFullDto getFullDto(Event event) {
        EventFullDto dto = EventMapper.toEventFullDto(event);
        dto.setViews(statisticService.getEventViewCount(event.getId()));
        dto.setConfirmedRequests(requestRepository.getConfirmCount(event.getId()));
        return dto;
    }

}
