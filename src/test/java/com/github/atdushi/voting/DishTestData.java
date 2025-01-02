package com.github.atdushi.voting;

import com.github.atdushi.MatcherFactory;
import com.github.atdushi.common.util.JsonUtil;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.to.DishTo;

import java.time.LocalDate;

import static com.github.atdushi.user.UserTestData.START_SEQ;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingComparingOnlyFieldsComparator(Dish.class, "id", "name");

    public static final LocalDate DATE = LocalDate.of(2020, 1, 30);

    public static final int LASAGNA_1_ID = START_SEQ + 5;
    public static final int LASAGNA_2_ID = START_SEQ + 6;
    public static final int LASAGNA_3_ID = START_SEQ + 7;
    public static final int LASAGNA_4_ID = START_SEQ + 8;
    public static final int SHASHLIK_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 100;

    public static final Dish lasagna1 = new Dish(LASAGNA_1_ID, "лазанья 1", 300, DATE);
    public static final Dish lasagna2 = new Dish(LASAGNA_2_ID, "лазанья 2", 300, DATE);
    public static final Dish lasagna3 = new Dish(LASAGNA_3_ID, "лазанья 3", 300, DATE);
    public static final Dish lasagna4 = new Dish(LASAGNA_4_ID, "лазанья 4", 300, DATE);
    public static final Dish shashlik = new Dish(SHASHLIK_ID, "шашлык", 400, DATE);

    public static Dish getNew() {
        Dish dish = new Dish(null, "лазанья 5", 300, LocalDate.now());
        dish.setRestaurant(RestaurantTestData.tokyoCity);
        return dish;
    }

    public static Dish getUpdated() {
        Dish dish = new Dish(LASAGNA_1_ID, "лазанья 1 новая", 330, DATE);
        dish.setRestaurant(RestaurantTestData.tokyoCity);
        return dish;
    }

    public static String jsonWithRestaurantId(DishTo dishTo, Integer restaurantId) {
        return JsonUtil.writeAdditionProps(dishTo, "restaurantId", restaurantId);
    }
}
