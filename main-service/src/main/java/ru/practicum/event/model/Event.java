package ru.practicum.event.model;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.event.type.EventState;
import ru.practicum.user.model.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @Column(name = "description", nullable = false, length = 4000)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "published", nullable = false)
    private LocalDateTime published;

    @Column(name = "location_lat", nullable = false)
    private float locationLat;

    @Column(name = "location_lon", nullable = false)
    private float locationLon;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @ManyToOne(optional = false)
    @JoinColumn (name = "category_id")
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn (name = "owner_id")
    private User owner;

}
