package ru.javaops.bootjava.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.javaops.bootjava.common.model.BaseEntity;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "vote")
@NoArgsConstructor
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @Column(name = "created", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime created;

    public Vote(User user, Restaurant restaurant) {
        this(null, user, restaurant, LocalDateTime.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant) {
        this(id, user, restaurant, LocalDateTime.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDateTime created) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "restaurant=" + restaurant.getName() +
                ", user=" + user.getName() +
                '}';
    }
}
