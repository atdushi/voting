package com.github.atdushi.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.atdushi.AbstractControllerTest;
import com.github.atdushi.user.UserTestData;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.atdushi.voting.DishTestData.*;
import static com.github.atdushi.voting.RestaurantTestData.TOKYO_CITY_ID;
import static com.github.atdushi.voting.web.DishController.REST_URL;

public class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + LASAGNA_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1, lasagna2, lasagna3, lasagna4, shashlik));;
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant?restaurantId=" + TOKYO_CITY_ID + "&date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1, lasagna2, lasagna3, lasagna4));
    }
}