package ru.javaops.bootjava.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.bootjava.common.to.NamedTo;

import java.util.Collections;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantToImpl extends NamedTo implements RestaurantTo {
    List<DishTo> dishes;

    private Integer rating;

    public RestaurantToImpl(Integer id, String name, List<DishTo> dishes, Integer rating) {
        super(id, name);
        this.dishes = dishes;
        this.rating = rating;
    }

    public RestaurantToImpl(Integer id, String name, Integer rating) {
        this(id, name, null, rating);
    }
}
