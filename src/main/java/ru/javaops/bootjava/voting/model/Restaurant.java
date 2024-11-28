package ru.javaops.bootjava.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.javaops.bootjava.common.model.NamedEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Setter
@Entity
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    //https://struchkov.dev/blog/ru/hibernate-multiple-bag-fetch-exception/
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
