package com.github.atdushi.voting.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import com.github.atdushi.common.model.NamedEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Setter
@Entity
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private Set<Vote> votes;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public List<Dish> getDishes() {
        return dishes == null ? Collections.emptyList() : dishes.stream().toList();
    }

    public List<Vote> getVotes() {
        return votes == null ? Collections.emptyList() : votes.stream().toList();
    }
}
