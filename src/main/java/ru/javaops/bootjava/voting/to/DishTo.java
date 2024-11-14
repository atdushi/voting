package ru.javaops.bootjava.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.bootjava.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    RestaurantTo restaurant;

    Double price;

    public DishTo(Integer id, String name, Double price, RestaurantTo restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }
}
