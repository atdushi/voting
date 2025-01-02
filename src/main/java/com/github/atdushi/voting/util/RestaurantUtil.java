package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.to.RestaurantTo;
import com.github.atdushi.voting.model.RestaurantWithRating;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantUtil {
    private static final int DEFAULT_RATING = 0;

    public static RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.getId(),
                restaurant.getName(),
                DishUtil.getTos(restaurant.getDishes()),
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

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantUtil::getTo)
                .collect(Collectors.toList());
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }
}
