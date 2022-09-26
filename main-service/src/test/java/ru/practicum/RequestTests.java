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
import ru.practicum.event.dto.Location;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.request.service.RequestService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class RequestTests {

    private final EntityManager em;
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestService requestService;

    @Test
    public void createAndGetRequest() {

        CategoryDto categoryDto = new CategoryDto(null, "category1");
        categoryDto = categoryService.create(categoryDto);

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        UserDto requesterDto = new UserDto(null, "user2", "user2@mail.ru");
        requesterDto = userService.create(requesterDto);

        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("annotation123")
                .category(categoryDto.getId())
                .description("description")
                .eventDate("2089-09-14 09:00:00")
                .location(new Location(0, 0))
                .title("title")
                .build();

        EventFullDto eventFullDto = eventService.create(userDto.getId(), newEventDto);

        eventService.publish(eventFullDto.getId());

        ParticipationRequestDto requestDto = requestService.createRequest(requesterDto.getId(),
                eventFullDto.getId());

        TypedQuery<Request> query = em.createQuery(
                     "Select r from Request r " +
                        "where r.eventId = :eventId and r.userId = :userId",
                Request.class);

        Request request = query
                .setParameter("eventId", eventFullDto.getId())
                .setParameter("userId", requesterDto.getId())
                .getSingleResult();

        Assertions.assertTrue(request != null);
        Assertions.assertEquals(request.getId(), requestDto.getId());

    }



}
