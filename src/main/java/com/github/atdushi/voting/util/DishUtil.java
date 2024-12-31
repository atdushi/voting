package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.to.DishTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DishUtil {

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream()
                .map(DishTo::new)
                .collect(Collectors.toList());
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        Dish dish = new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
        if (dishTo.getRestaurantId() != null) {
            dish.setRestaurant(new Restaurant(dishTo.getRestaurantId(), null));
        }
        return dish;
    }
}
