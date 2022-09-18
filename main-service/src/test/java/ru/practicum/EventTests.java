package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventFilterParams;
import ru.practicum.event.service.EventService;
import org.junit.jupiter.api.Test;
import ru.practicum.event.type.EventSort;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class EventTests {

    private final EntityManager em;
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Test
    public void createAndGetEvent() {

        CategoryDto categoryDto = new CategoryDto(null, "category1");
        categoryDto = categoryService.create(categoryDto);

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("Annotation")
                .category(categoryDto.getId())
                .description("description")
                .eventDate("2039-09-14 09:00:00")
                .location(new Location(0, 0))
                .title("title")
                .build();

        EventFullDto eventFullDto = eventService.create(userDto.getId(), newEventDto);

        TypedQuery<Event> query = em.createQuery("Select e from Event e where e.annotation = :annotation",
                Event.class);

        Event event = query.setParameter("annotation", newEventDto.getAnnotation()).getSingleResult();

        Assertions.assertTrue(event != null);
        Assertions.assertEquals(event.getId(), eventFullDto.getId());

    }

    @Test
    public void findEvent() {

        CategoryDto categoryDto = new CategoryDto(null, "category1");
        categoryDto = categoryService.create(categoryDto);

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("Экзотическая рыбалка в Сахаре")
                .category(categoryDto.getId())
                .description("description")
                .eventDate("2024-01-01 09:00:00")
                .location(new Location(0, 0))
                .title("title")
                .build();

        EventFullDto eventFullDto = eventService.create(userDto.getId(), newEventDto);

        EventFilterParams filterParams = EventFilterParams.builder().text("САХАР").build();

        List<EventShortDto> list = eventService.findEventsAsShort(filterParams,
                EventSort.EVENT_DATE, 0, 10);

        Assertions.assertEquals(1, list.size());

    }

    @Test
    public void updateEvent() {

        CategoryDto categoryDto = new CategoryDto(null, "category1");
        categoryDto = categoryService.create(categoryDto);

        UserDto userDto = new UserDto(null, "user1", "user1@mail.ru");
        userDto = userService.create(userDto);

        NewEventDto newEventDto = NewEventDto.builder()
                .annotation("Annotation")
                .category(categoryDto.getId())
                .description("description")
                .eventDate("2039-09-14 09:00:00")
                .location(new Location(0, 0))
                .title("title")
                .build();

        EventFullDto eventFullDto = eventService.create(userDto.getId(), newEventDto);

        UpdateEventRequest updateEventRequest = new UpdateEventRequest();
        updateEventRequest.setEventId(eventFullDto.getId());
        updateEventRequest.setAnnotation("change annotation");
        eventService.update(userDto.getId(), updateEventRequest);

        TypedQuery<Event> query = em.createQuery("Select e from Event e where e.id = :id",
                Event.class);

        Event event = query.setParameter("id", eventFullDto.getId()).getSingleResult();

        Assertions.assertTrue(event != null);
        Assertions.assertEquals(event.getAnnotation(), updateEventRequest.getAnnotation());

    }


}
