package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.MatcherFactory;
import ru.javaops.bootjava.voting.model.Dish;

import static ru.javaops.bootjava.user.UserTestData.START_SEQ;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingComparingOnlyFieldsComparator(Dish.class, "id", "name");

    public static final int LASAGNA_1_ID = START_SEQ + 5;
    public static final int SHASHLIK_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 100;

    public static final Dish lasagna1 = new Dish(LASAGNA_1_ID, "лазанья 1", 300.0);
    public static final Dish shashlik = new Dish(SHASHLIK_ID, "шашлык", 400.0);
}
