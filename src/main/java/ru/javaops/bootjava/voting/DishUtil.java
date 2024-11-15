package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.DishTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DishUtil {

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return getTos(dishes, false);
    }

    public static List<DishTo> getTos(Collection<Dish> dishes, boolean includeRestaurant) {
        return dishes.stream()
                .map(dish -> DishUtil.createTo(dish, includeRestaurant))
                .collect(Collectors.toList());
    }

    public static DishTo createTo(Dish dish) {
        return createTo(dish, false);
    }

    public static DishTo createTo(Dish dish, boolean includeRestaurant) {
        Restaurant restaurant = dish.getRestaurant();

        return new DishTo(
                dish.getId(),
                dish.getName(),
                dish.getPrice(),
                includeRestaurant ? RestaurantUtil.createTo(restaurant) : null
        );
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
    }
}
