package com.github.atdushi.voting.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.voting.View;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonView(View.Create.class)
    private User user;

    @Column(name = "vote_date", nullable = false, columnDefinition = "date default current_date")
    private LocalDate date;

    public Vote(User user, Restaurant restaurant) {
        this(null, user, restaurant, LocalDate.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant) {
        this(id, user, restaurant, LocalDate.now());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate date) {
        this.id = id;
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "restaurant=" + restaurant.getName() +
                ", user=" + user.getName() +
                '}';
    }
}
