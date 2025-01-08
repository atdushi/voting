package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.RestaurantWithRating;
import com.github.atdushi.voting.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

public class RestaurantUtil {

    public static RestaurantTo getTo(Restaurant restaurant) {
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
                restaurantWithRating.getRating());
    }

    public static <T extends RestaurantWithRating> List<RestaurantWithRating> getTos(Collection<T> restaurants) {
        return restaurants.stream()
                .map(r -> (RestaurantWithRating) RestaurantUtil.getTo(r))
                .toList();
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants, boolean includeDishes) {
        return restaurants.stream()
                .map(r -> getTo(r, includeDishes))
                .toList();
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }

    public static Restaurant createNewFromId(Integer restaurantId) {
        return new Restaurant(restaurantId, null);
    }
}
