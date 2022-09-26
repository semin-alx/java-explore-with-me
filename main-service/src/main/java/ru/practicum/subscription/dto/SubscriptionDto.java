package ru.practicum.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDto {

    private Long id;

    private UserShortDto user;

    private UserShortDto friend;

}
