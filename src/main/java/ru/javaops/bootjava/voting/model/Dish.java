package ru.javaops.bootjava.voting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.javaops.bootjava.common.model.NamedEntity;

import java.util.Date;

@Entity
@Table(name = "dish")
@NoArgsConstructor
public class Dish extends NamedEntity {

    @Getter
    @Setter
    @Column(name = "price", nullable = false, columnDefinition = "decimal")
    private Double price;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Getter
    @Setter
    @Column(name = "date", nullable = false, columnDefinition = "date default current_date")
    private Date date;

//    @Getter
//    @Setter
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
