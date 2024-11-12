package ru.javaops.bootjava.voting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.javaops.bootjava.common.model.NamedEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
public class Restaurant extends NamedEntity {

    @Setter
//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Dish> dishes;

    @Getter
    @Setter
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    //https://struchkov.dev/blog/ru/hibernate-multiple-bag-fetch-exception/
    private Set<Vote> votes;

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public List<Dish> getDishes() {
        return dishes == null ? Collections.emptyList() : dishes.stream().toList();
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                '}';
    }
}
