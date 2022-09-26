package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.Location;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;
import ru.practicum.subscription.dto.SubscriptionDto;
import ru.practicum.subscription.model.Subscription;
import ru.practicum.subscription.service.SubscriptionService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class SubscriptionTests {

    private final EntityManager em;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final RequestService requestService;

    @Test
    public void createSubscription() {

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        UserDto friendDto = new UserDto(null, "user2", "user2@mail.ru");
        friendDto = userService.create(friendDto);

        SubscriptionDto subscriptionDto = subscriptionService.create(userDto.getId(),
                friendDto.getId());

        TypedQuery<Subscription> query = em.createQuery(
                "Select s from Subscription s " +
                        "where s.user.id = :userId and s.friend.id = :friendId",
                Subscription.class);

        Subscription subscription = query
                .setParameter("userId", userDto.getId())
                .setParameter("friendId", friendDto.getId())
                .getSingleResult();

        Assertions.assertEquals(subscription.getId(), subscription.getId());

    }

    @Test
    public void deleteSubscription() {

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        UserDto friendDto = new UserDto(null, "user2", "user2@mail.ru");
        friendDto = userService.create(friendDto);

        SubscriptionDto subscriptionDto = subscriptionService.create(userDto.getId(),
                friendDto.getId());

        subscriptionService.deleteById(subscriptionDto.getId());

        TypedQuery<Subscription> query = em.createQuery(
                "Select s from Subscription s " +
                        "where s.id = :id",
                Subscription.class);

        List<Subscription> subscriptionList = query
                .setParameter("id", subscriptionDto.getId())
                .getResultList();

        Assertions.assertTrue(subscriptionList.size() == 0);

    }

    @Test
    public void getSubscriptionList() {

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        UserDto friendDto = new UserDto(null, "user2", "user2@mail.ru");
        friendDto = userService.create(friendDto);

        SubscriptionDto subscriptionDto = subscriptionService.create(userDto.getId(),
                friendDto.getId());

        List<SubscriptionDto> list = subscriptionService.getSubscriptionList(userDto.getId());

        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(subscriptionDto.getId(), list.get(0).getId());

    }

    @Test
    public void getEventList() {

        CategoryDto categoryDto = new CategoryDto(null, "category1");
        categoryDto = categoryService.create(categoryDto);

        UserDto eventOwnerDto = new UserDto(null, "user1", "user1@mail.ru");
        eventOwnerDto = userService.create(eventOwnerDto);

        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("Annotation")
                .category(categoryDto.getId())
                .description("description")
                .eventDate("2039-09-14 09:00:00")
                .location(new Location(0, 0))
                .title("title")
                .build();

        EventFullDto eventFullDto = eventService.create(eventOwnerDto.getId(), newEventDto);

        eventService.publish(eventFullDto.getId());

        UserDto friendDto = new UserDto(null, "user2", "user2@mail.ru");
        friendDto = userService.create(friendDto);

        ParticipationRequestDto requestDto = requestService.createRequest(friendDto.getId(),
                eventFullDto.getId());

        requestService.approveRequest(eventOwnerDto.getId(), eventFullDto.getId(),
                requestDto.getId());

        UserDto userDto = new UserDto(null, "user3", "user3@mail.ru");
        userDto = userService.create(userDto);

        SubscriptionDto subscriptionDto = subscriptionService.create(userDto.getId(),
                friendDto.getId());

        List<EventShortDto> events = subscriptionService.getEventList(userDto.getId());

        Assertions.assertEquals(1, events.size());
        Assertions.assertEquals(eventFullDto.getId(), events.get(0).getId());

    }

}
