package com.github.atdushi.voting.util;

import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.to.RestaurantTo;
import com.github.atdushi.voting.model.RestaurantWithRating;

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
