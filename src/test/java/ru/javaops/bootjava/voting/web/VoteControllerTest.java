package ru.javaops.bootjava.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaops.bootjava.AbstractControllerTest;
import ru.javaops.bootjava.user.UserTestData;
import ru.javaops.bootjava.voting.VoteTestData;
import ru.javaops.bootjava.voting.VoteUtil;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.VoteRepository;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaops.bootjava.voting.RestaurantTestData.TOKYO_CITY_ID;

public class VoteControllerTest extends AbstractControllerTest {
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getByUserIdAndRestaurantId() throws Exception {
        Vote byUserIdAndRestaurantId = voteRepository.getByUserIdAndRestaurantId(UserTestData.USER_ID, TOKYO_CITY_ID, VoteTestData.VOTE_DATE);
        assertNotNull(byUserIdAndRestaurantId);
    }

    @Test
    void checkExists() throws Exception {
        Vote vote = VoteUtil.createNew(100000, 100003);
        boolean b = voteRepository.checkExists(vote);
        assertTrue(b);

        vote = VoteUtil.createNew(100000, 100004);
        b = voteRepository.checkExists(vote);
        assertFalse(b);
    }
}
