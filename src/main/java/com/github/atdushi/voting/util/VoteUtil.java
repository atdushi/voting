package com.github.atdushi.voting.util;

import com.github.atdushi.user.model.User;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.to.VoteTo;

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
