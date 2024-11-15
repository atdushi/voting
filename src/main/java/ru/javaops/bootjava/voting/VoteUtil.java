package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.user.model.User;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.model.Vote;

public class VoteUtil {
    public static Vote createNew(Integer userId, Integer restaurantId) {
        User user = new User(userId, null, null, null);
        Restaurant restaurant = new Restaurant(restaurantId, null);
        return new Vote(user, restaurant);
    }
}
