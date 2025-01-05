package com.github.atdushi.voting;

import com.github.atdushi.MatcherFactory;
import com.github.atdushi.user.UserTestData;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.to.VoteTo;

import java.time.LocalDate;

import static com.github.atdushi.user.UserTestData.START_SEQ;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingComparingOnlyFieldsComparator(Vote.class, "id", "restaurant.id", "user.id");

    public static final MatcherFactory.Matcher<VoteTo> VOTETO_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static LocalDate VOTE_DATE = LocalDate.of(2020, 1, 30);

    public static final int TOKYO_VOTE_1_ID = START_SEQ + 14;
    public static final int TOKYO_VOTE_2_ID = START_SEQ + 15;

    public static Vote TokyoVote1 = new Vote(TOKYO_VOTE_1_ID, UserTestData.user, RestaurantTestData.tokyoCity);
    public static VoteTo TokyoVoteTo1 = new VoteTo(TOKYO_VOTE_1_ID, UserTestData.user.getId(), RestaurantTestData.tokyoCity.getId(), VOTE_DATE);

    public static Vote getNew() {
        return new Vote(null, UserTestData.user, RestaurantTestData.bahroma);
    }

    public static Vote getUpdated() {
        return new Vote(TOKYO_VOTE_2_ID, UserTestData.user, RestaurantTestData.bahroma);
    }
}
