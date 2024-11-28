package ru.javaops.bootjava.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.javaops.bootjava.common.model.NamedEntity;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "dish")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false, columnDefinition = "decimal")
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference // do not serialize
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, columnDefinition = "date default current_date")
    private Date date;

//    @Column(name = "restaurant_id", nullable = false)
//    private int restaurantId;

    public Dish(Integer id, String name, Double price) {
        this(id, name, price, new Date());
    }

    public Dish(Integer id, String name, Double price, Date date) {
        super(id, name);
        this.price = price;
        this.date = date;
    }
}
