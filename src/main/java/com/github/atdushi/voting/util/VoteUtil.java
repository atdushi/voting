package com.github.atdushi.voting.util;

import com.github.atdushi.user.model.User;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.to.VoteTo;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {

    public static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    public static Vote createNew(Integer userId, Integer restaurantId) {
        User user = new User(userId, null, null, null);
        Restaurant restaurant = new Restaurant(restaurantId, null);
        return new Vote(user, restaurant);
    }

    public static VoteTo getTo(Vote vote) {
        return new VoteTo(
                vote.getId(),
                vote.getUser().id(),
                vote.getRestaurant().id(),
                vote.getDate());
    }

    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::getTo)
                .collect(Collectors.toList());
    }
}
