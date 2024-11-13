package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.DishTo;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DishUtil {

    public static List<DishTo> getTos(Collection<Dish> dishes){
        return dishes.stream()
                .map(DishUtil::createTo)
                .collect(Collectors.toList());
    }

    public static DishTo createTo(Dish dish) {
        Restaurant restaurant = dish.getRestaurant();
        return new DishTo(
                dish.getId(),
                dish.getName(),
                new RestaurantTo(restaurant.getId(), restaurant.getName(), null));
    }
}
