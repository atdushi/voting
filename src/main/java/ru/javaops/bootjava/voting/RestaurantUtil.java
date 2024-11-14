package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.RestaurantTo;
import ru.javaops.bootjava.voting.to.RestaurantToImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {

    public static List<RestaurantTo> unproxy(Collection<RestaurantTo> restaurants) {
        return restaurants.stream()
                .map(m -> new RestaurantToImpl(m.getId(), m.getName(), m.getDishes(), m.getRating()))
                .collect(Collectors.toList());
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return getTos(restaurants, false, false);
    }

    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants, boolean includeDishes, boolean includeRating) {
        return restaurants.stream()
                .map(m -> RestaurantUtil.createTo(m, includeDishes, includeRating))
                .collect(Collectors.toList());
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return createTo(restaurant, false, false);
    }

    public static RestaurantTo createTo(Restaurant restaurant, boolean includeDishes, boolean includeRating) {
        return new RestaurantToImpl(
                restaurant.getId(),
                restaurant.getName(),
                includeDishes ? DishUtil.getTos(restaurant.getDishes()) : null,
                includeRating ? restaurant.getVotes().size() : null
        );
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }
}
