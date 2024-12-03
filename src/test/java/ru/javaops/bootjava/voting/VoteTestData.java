package ru.javaops.bootjava.voting;

import ru.javaops.bootjava.MatcherFactory;
import ru.javaops.bootjava.user.UserTestData;
import ru.javaops.bootjava.voting.model.Vote;

import java.time.LocalDate;

import static ru.javaops.bootjava.user.UserTestData.START_SEQ;

public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingComparingOnlyFieldsComparator(Vote.class, "id", "restaurant.id", "user.id");

    public static LocalDate VOTE_DATE = LocalDate.of(2020, 1, 30);

    public static final int TOKYO_VOTE_1_ID = START_SEQ + 10;
    public static final int TOKYO_VOTE_2_ID = START_SEQ + 11;

    public static Vote TokyoVote1 = new Vote(TOKYO_VOTE_1_ID, UserTestData.user, RestaurantTestData.tokyoCity);

    public static Vote getNew() {
        return new Vote(null, UserTestData.user, RestaurantTestData.bahroma);
    }

    public static Vote getUpdated() {
        return new Vote(TOKYO_VOTE_2_ID, UserTestData.user, RestaurantTestData.bahroma);
    }
}
