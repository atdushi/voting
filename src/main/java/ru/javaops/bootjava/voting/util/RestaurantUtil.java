package ru.javaops.bootjava.voting.util;

import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.RestaurantTo;
import ru.javaops.bootjava.voting.model.RestaurantWithRating;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static List<RestaurantTo> getTos(Collection<RestaurantWithRating> restaurants) {
        return restaurants.stream()
                .map(RestaurantTo::new)
                .collect(Collectors.toList());
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantTo::new)
                .collect(Collectors.toList());
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }
}
