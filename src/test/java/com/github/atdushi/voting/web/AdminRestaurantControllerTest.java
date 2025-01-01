package com.github.atdushi.voting.web;

import com.github.atdushi.voting.util.RestaurantUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.atdushi.AbstractControllerTest;
import com.github.atdushi.common.util.JsonUtil;
import com.github.atdushi.user.UserTestData;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.repository.RestaurantRepository;
import com.github.atdushi.voting.to.RestaurantTo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.atdushi.voting.RestaurantTestData.*;
import static com.github.atdushi.voting.web.AdminRestaurantController.REST_URL;

public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + TOKYO_CITY_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(TOKYO_CITY_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        RestaurantTo restaurantTo = RestaurantUtil.getTo(updated);
        String json = JsonUtil.writeValue(restaurantTo);

        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TOKYO_CITY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(repository.getExisted(TOKYO_CITY_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createNew() throws Exception {
        Restaurant newRestaurant = getNew();
        RestaurantTo restaurantTo = RestaurantUtil.getTo(newRestaurant);
        String json = JsonUtil.writeValue(restaurantTo);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.getExisted(newId), newRestaurant);
    }
}
