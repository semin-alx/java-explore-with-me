package ru.practicum.subscription.helper;

import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.user.helper.UserMapper;

public class SubscriptionMapper {

    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .user(UserMapper.toUserShortDto(subscription.getUser()))
                .friend(UserMapper.toUserShortDto(subscription.getFriend()))
                .build();
    }

}
