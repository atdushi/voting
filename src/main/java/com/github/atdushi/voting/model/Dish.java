package com.github.atdushi.voting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import com.github.atdushi.common.model.NamedEntity;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "dish")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false, columnDefinition = "integer")
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
//    @JsonBackReference // do not serialize
    private Restaurant restaurant;

    @Column(name = "dish_date", nullable = false)
    private Date date;

    public Dish(Integer id, String name, Integer price) {
        this(id, name, price, new Date());
    }

    public Dish(Integer id, String name, Integer price, Date date) {
        super(id, name);
        this.price = price;
        this.date = date;
    }
}
