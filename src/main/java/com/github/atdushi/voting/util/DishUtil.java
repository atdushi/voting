package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.to.DishTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DishUtil {

    public static DishTo getTo(Dish dish) {
        return new DishTo(
                dish.getId(),
                dish.getName(),
                dish.getPrice(),
                dish.getRestaurant() != null ? dish.getRestaurant().getId() : null,
                dish.getDate());
    }

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream()
                .map(DishUtil::getTo)
                .collect(Collectors.toList());
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        Dish dish = new Dish(
                dishTo.getId(),
                dishTo.getName(),
                dishTo.getPrice(),
                new Restaurant(dishTo.getRestaurantId(), null),
                dishTo.getDate());

        if (dishTo.getRestaurantId() != null) {
            dish.setRestaurant(new Restaurant(dishTo.getRestaurantId(), null));
        }
        return dish;
    }
}
