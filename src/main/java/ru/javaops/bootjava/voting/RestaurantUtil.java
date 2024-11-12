package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::createTo).collect(Collectors.toList());
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.getId(),
                restaurant.getName()
                //, DishUtil.getTos(restaurant.getDishes())
        );
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }
}
