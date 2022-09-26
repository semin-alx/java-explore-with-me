package ru.practicum.subscription.model;

import lombok.*;
import ru.practicum.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn (name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn (name = "friend_id")
    private User friend;

}
