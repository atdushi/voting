package com.github.atdushi.voting.web;

import com.github.atdushi.AbstractControllerTest;
import com.github.atdushi.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.atdushi.voting.RestaurantTestData.*;
import static com.github.atdushi.voting.web.RestaurantController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TOKYO_CITY_ID + "?date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(tokyoCity));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "?date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(bahroma, tokyoCity));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllByRating() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "order-by-rating-desc?date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(tokyoCity, bahroma));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getTopRanked() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "top-ranked?date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(tokyoCity));
    }
}
