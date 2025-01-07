package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.to.RestaurantTo;
import com.github.atdushi.voting.model.RestaurantWithRating;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {
    private static final int DEFAULT_RATING = 0;

    public static RestaurantTo getTo(Restaurant restaurant){
        return getTo(restaurant, false);
    }

    public static RestaurantTo getTo(Restaurant restaurant, boolean includeDishes) {
        return new RestaurantTo(
                restaurant.getId(),
                restaurant.getName(),
                includeDishes ? DishUtil.getTos(restaurant.getDishes()) : null,
                null);
    }

    public static RestaurantTo getTo(RestaurantWithRating restaurantWithRating) {
        return new RestaurantTo(
                restaurantWithRating.getId(),
                restaurantWithRating.getName(),
                null, // does not include dishes
                restaurantWithRating.getRating() == null ? DEFAULT_RATING : restaurantWithRating.getRating());
    }

    public static List<RestaurantTo> getTos(Collection<RestaurantWithRating> restaurants) {
        return restaurants.stream()
                .map(RestaurantUtil::getTo)
                .collect(Collectors.toList());
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants, boolean includeDishes) {
        return restaurants.stream()
                .map(r -> getTo(r, includeDishes))
                .collect(Collectors.toList());
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }

    public static Restaurant createNewFromId(Integer restaurantId) {
        return new Restaurant(restaurantId, null);
    }
}
