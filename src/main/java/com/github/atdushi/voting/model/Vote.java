package com.github.atdushi.voting.model;

import lombok.*;

import jakarta.persistence.*;
import com.github.atdushi.common.model.BaseEntity;
import com.github.atdushi.user.model.User;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "vote")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created", nullable = false, columnDefinition = "date default now()")
    private LocalDate created;

    public Vote(User user, Restaurant restaurant) {
        this(null, user, restaurant, LocalDate.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant) {
        this(id, user, restaurant, LocalDate.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate created) {
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
