package ru.javaops.bootjava.voting.util;

import ru.javaops.bootjava.user.model.User;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.to.VoteTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {
    public static Vote createNew(Integer userId, Integer restaurantId) {
        User user = new User(userId, null, null, null);
        Restaurant restaurant = new Restaurant(restaurantId, null);
        return new Vote(user, restaurant);
    }

    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VoteTo::new)
                .collect(Collectors.toList());
    }
}
