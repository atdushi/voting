package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.DishTo;

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
