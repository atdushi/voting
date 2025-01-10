package com.github.atdushi.voting.web;

import com.github.atdushi.AbstractControllerTest;
import com.github.atdushi.common.util.JsonUtil;
import com.github.atdushi.user.UserTestData;
import com.github.atdushi.voting.RestaurantTestData;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.repository.VoteRepository;
import com.github.atdushi.voting.to.VoteTo;
import com.github.atdushi.voting.util.VoteUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

import static com.github.atdushi.voting.VoteTestData.*;
import static com.github.atdushi.voting.web.VoteController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    private static final int EPOCH_SECOND = 1643533200; // 2022-01-30
    private static final LocalTime ALLOWED_TIME = VoteUtil.TIME_LIMIT.minusHours(1L);
    private static final LocalTime FORBIDDEN_TIME = VoteUtil.TIME_LIMIT.plusHours(1L);

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
    void getVotingHistory() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(
                REST_URL_SLASH + "history"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<?> list = JsonUtil.readValue(action.andReturn().getResponse().getContentAsString(), List.class);
        assert !list.isEmpty();
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getToday() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(
                REST_URL_SLASH + "today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        VoteTo voteTo = JsonUtil.readValue(action.andReturn().getResponse().getContentAsString(), VoteTo.class);
        assert voteTo.getRestaurantId() == TokyoVote1.getRestaurant().id();
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void countByRestaurant() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(
                REST_URL_SLASH + "count-by-restaurant?restaurantId=" + RestaurantTestData.TOKYO_CITY_ID + "&date=" + VOTE_DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Integer i = JsonUtil.readValue(action.andReturn().getResponse().getContentAsString(), Integer.class);
        assert i == 2;
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void revoteAllowed() throws Exception {
        Clock spyClock = Mockito.spy(Clock.systemDefaultZone());

        try (MockedStatic<Clock> clockMock = Mockito.mockStatic(Clock.class);
             MockedStatic<LocalTime> timeMock = Mockito.mockStatic(LocalTime.class)) {

            timeMock.when(LocalTime::now).thenReturn(ALLOWED_TIME);
            clockMock.when(Clock::systemDefaultZone).thenReturn(spyClock);
            clockMock.when(Clock::systemUTC).thenReturn(spyClock);
            Mockito.when(spyClock.instant()).thenReturn(Instant.ofEpochSecond(EPOCH_SECOND));

            Vote newVote = getNew();
            vote(newVote);
            perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().getId()))
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void revoteForbidden() throws Exception {
        Clock spyClock = Mockito.spy(Clock.systemDefaultZone());

        try (MockedStatic<Clock> clockMock = Mockito.mockStatic(Clock.class);
             MockedStatic<LocalTime> timeMock = Mockito.mockStatic(LocalTime.class)) {

            timeMock.when(LocalTime::now).thenReturn(FORBIDDEN_TIME);
            clockMock.when(Clock::systemDefaultZone).thenReturn(spyClock);
            clockMock.when(Clock::systemUTC).thenReturn(spyClock);
            Mockito.when(spyClock.instant()).thenReturn(Instant.ofEpochSecond(EPOCH_SECOND));

            Vote newVote = getNew();
            vote(newVote);
            perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().getId()))
                    .andExpect(status().isUnprocessableEntity());

        }
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createNew() throws Exception {
        Clock spyClock = Mockito.spy(Clock.systemDefaultZone());

        try (MockedStatic<Clock> clockMock = Mockito.mockStatic(Clock.class);
             MockedStatic<LocalTime> timeMock = Mockito.mockStatic(LocalTime.class)) {

            timeMock.when(LocalTime::now).thenReturn(ALLOWED_TIME);
            clockMock.when(Clock::systemDefaultZone).thenReturn(spyClock);
            clockMock.when(Clock::systemUTC).thenReturn(spyClock);
            Mockito.when(spyClock.instant()).thenReturn(Instant.ofEpochSecond(EPOCH_SECOND));

            Vote newVote = getNew();
            VoteTo created = vote(newVote);

            assert newVote.getRestaurant().id() == created.getRestaurantId();
            assert newVote.getUser().id() == created.getUserId();
        }
    }

    private VoteTo vote(Vote newVote) throws Exception {
        assert newVote.getRestaurant().getId() != null;
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().getId()))
                .andExpect(status().isCreated());
        return JsonUtil.readValue(action.andReturn().getResponse().getContentAsString(), VoteTo.class);
    }
}
