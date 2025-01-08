package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.to.DishTo;

import java.util.Collection;
import java.util.List;

public class DishUtil {

    public static DishTo getTo(Dish dish) {
        return new DishTo(
                dish.getId(),
                dish.getName(),
                dish.getPrice(),
                dish.getRestaurant() != null ? dish.getRestaurant().id() : 0,
                dish.getDate());
    }

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream()
                .map(DishUtil::getTo)
                .toList();
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        Dish dish = new Dish(
                dishTo.getId(),
                dishTo.getName(),
                dishTo.getPrice(),
                new Restaurant(dishTo.getRestaurantId(), null),
                dishTo.getDate());

        if (dishTo.getRestaurantId() > 0) {
            dish.setRestaurant(new Restaurant(dishTo.getRestaurantId(), null));
        }
        return dish;
    }
}
