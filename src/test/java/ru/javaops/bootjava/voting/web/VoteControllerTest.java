package ru.javaops.bootjava.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.AbstractControllerTest;
import ru.javaops.bootjava.common.util.JsonUtil;
import ru.javaops.bootjava.user.UserTestData;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.VoteRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.voting.VoteTestData.*;
import static ru.javaops.bootjava.voting.web.VoteController.REST_URL;

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TOKYO_VOTE_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTETO_MATCHER.contentJson(TokyoVoteTo1));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void countByRestaurant() throws Exception {
        Vote newVote = getNew();
        Vote created = vote(newVote);

        ResultActions action = perform(MockMvcRequestBuilders.get(
                REST_URL_SLASH + "count-by-restaurant?restaurantId=" + created.getRestaurant().getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Integer i = JsonUtil.readValue(action.andReturn().getResponse().getContentAsString(), Integer.class);
        assert i == 1;
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createNew() throws Exception {
        Vote newVote = getNew();
        Vote created = vote(newVote);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);
    }

    private Vote vote(Vote newVote) throws Exception {
        assert newVote.getRestaurant().getId() != null;
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newVote.getRestaurant().getId().toString()))
                .andExpect(status().isCreated());
        return VOTE_MATCHER.readFromJson(action);
    }
}
