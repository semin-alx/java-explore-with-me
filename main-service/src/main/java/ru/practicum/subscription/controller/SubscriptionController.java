package ru.practicum.subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.service.SubscriptionService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/subscription")
@Validated
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping(value = "/user/{userId}/friend/{friendId}")
    public SubscriptionDto create(@PathVariable @Positive long userId,
                                  @PathVariable @Positive long friendId) {
        return subscriptionService.create(userId, friendId);
    }

    @GetMapping(value = "/user/{userId}")
    public List<SubscriptionDto> getSubscriptionList(@PathVariable @Positive long userId) {
        return subscriptionService.getSubscriptionList(userId);
    }

    @DeleteMapping(value = "/{subscriptionId}")
    public void delete(@PathVariable @Positive long subscriptionId) {
        subscriptionService.deleteById(subscriptionId);
    }

    @GetMapping(value = "/user/{userId}/events")
    public List<EventShortDto>  getEventList(@PathVariable @Positive long userId) {
        return subscriptionService.getEventList(userId);
    }

}
