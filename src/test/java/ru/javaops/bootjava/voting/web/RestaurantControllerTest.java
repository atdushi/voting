package ru.javaops.bootjava.voting.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.AbstractControllerTest;
import ru.javaops.bootjava.voting.VoteUtil;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.repository.VoteRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.voting.RestaurantTestData.*;
import static ru.javaops.bootjava.voting.web.RestaurantController.REST_URL;

public class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getByUserIdAndRestaurantId() throws Exception {
        Vote byUserIdAndRestaurantId = voteRepository.getByUserIdAndRestaurantId(100000, 100003);

    }

    @Test
    void checkExists() throws Exception {
        Vote vote = VoteUtil.createNew(100000, 100003);
        boolean b = voteRepository.checkExists(vote);
        Assertions.assertTrue(b);

        vote = VoteUtil.createNew(100000, 100004);
        b = voteRepository.checkExists(vote);
        Assertions.assertFalse(b);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TOKYO_CITY_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(tokyoCity));
    }

}
