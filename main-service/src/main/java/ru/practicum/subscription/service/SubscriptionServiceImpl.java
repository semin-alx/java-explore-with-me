package ru.practicum.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.error.exception.ObjectNotFoundException;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.helper.EventMapper;
import ru.practicum.event.service.EventService;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.helper.SubscriptionMapper;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.subscription.repository.SubscriptionRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventService eventService;

    @Override
    public SubscriptionDto create(long userId, long friendId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Значение идентификатора " +
                        "пользователя userId в базе не найдено"));

        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ObjectNotFoundException("Значение идентификатора " +
                        "пользователя friendId в базе не найдено"));

        Subscription subscription = Subscription.builder()
                .user(user)
                .friend(friend)
                .build();

        return SubscriptionMapper.toSubscriptionDto(subscriptionRepository.save(subscription));

    }

    @Override
    public List<SubscriptionDto> getSubscriptionList(long userId) {

        return subscriptionRepository.findByUserId(userId).stream()
                .map(SubscriptionMapper::toSubscriptionDto)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteById(long subscriptionId) {

        subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ObjectNotFoundException("Указанная подписка не найдена"));

        subscriptionRepository.deleteById(subscriptionId);

    }

    @Override
    public List<EventShortDto> getEventList(long userId) {

        List<EventShortDto> shortDtoList =
                subscriptionRepository.findEventsByUserId(userId).stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList());

        eventService.enrichDtoList(shortDtoList);

        return shortDtoList;

    }

}
