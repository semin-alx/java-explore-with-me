package ru.practicum.request.model;

import lombok.*;
import ru.practicum.request.type.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
