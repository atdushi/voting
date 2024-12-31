package com.github.atdushi.voting;

import com.github.atdushi.MatcherFactory;
import com.github.atdushi.voting.model.Restaurant;

import java.time.LocalDate;

import static com.github.atdushi.user.UserTestData.START_SEQ;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "votes", "dishes");// "dishes.restaurant", "dishes.date");

    public static final LocalDate DATE = LocalDate.of(2020, 1, 30);

    public static final int TOKYO_CITY_ID = START_SEQ + 3;
    public static final int BAHROMA_ID = START_SEQ + 4;
    public static final int NOT_FOUND = 100;

    public static final Restaurant tokyoCity;
    public static final Restaurant bahroma;

    static {
        tokyoCity = new Restaurant(TOKYO_CITY_ID, "ТОКИО-CITY");
//        tokyoCity.setDishes(Set.of(DishTestData.lasagna));

        bahroma = new Restaurant(BAHROMA_ID, "Bahroma");
//        bahroma.setDishes(Set.of(DishTestData.shashlik));
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "New");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(TOKYO_CITY_ID, "ТОКИО-CITY New");
    }
}
