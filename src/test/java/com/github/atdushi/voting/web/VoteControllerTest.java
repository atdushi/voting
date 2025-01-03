package com.github.atdushi.voting.web;

import com.github.atdushi.AbstractControllerTest;
import com.github.atdushi.common.util.JsonUtil;
import com.github.atdushi.user.UserTestData;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.repository.VoteRepository;
import com.github.atdushi.voting.util.VoteUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static com.github.atdushi.voting.VoteTestData.*;
import static com.github.atdushi.voting.web.VoteController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void revoteAllowed() throws Exception {
        LocalTime allowedTime = VoteUtil.TIME_LIMIT.minusHours(1L);
        try (MockedStatic<LocalTime> timeMock = Mockito.mockStatic(LocalTime.class)) {
            timeMock.when(LocalTime::now).thenReturn(allowedTime);
            Vote newVote = getNew();
            vote(newVote);
            perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().getId()))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void revoteForbidden() throws Exception {
        LocalTime forbiddenTime = VoteUtil.TIME_LIMIT.plusHours(1L);
        try (MockedStatic<LocalTime> timeMock = Mockito.mockStatic(LocalTime.class)) {
            timeMock.when(LocalTime::now).thenReturn(forbiddenTime);
            Vote newVote = getNew();
            vote(newVote);
            perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().getId()))
                    .andExpect(status().isUnprocessableEntity());
        }
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
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().getId()))
                .andExpect(status().isCreated());
        return VOTE_MATCHER.readFromJson(action);
    }
}
