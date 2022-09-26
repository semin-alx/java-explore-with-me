package ru.practicum.subscription.service;

import ru.practicum.event.dto.EventShortDto;
import ru.practicum.subscription.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto create(long userId, long friendId);

    List<SubscriptionDto> getSubscriptionList(long userId);

    void deleteById(long subscriptionId);

    List<EventShortDto> getEventList(long userId);

}
