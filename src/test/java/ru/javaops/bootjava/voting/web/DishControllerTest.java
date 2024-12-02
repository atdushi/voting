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
import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.repository.DishRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.voting.DishTestData.*;
import static ru.javaops.bootjava.voting.RestaurantTestData.TOKYO_CITY_ID;
import static ru.javaops.bootjava.voting.web.DishController.REST_URL;

public class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private DishRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + LASAGNA_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant?restaurantId=" + TOKYO_CITY_ID + "&date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1, lasagna2, lasagna3, lasagna4));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + LASAGNA_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(LASAGNA_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + LASAGNA_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(repository.getExisted(LASAGNA_1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createNew() throws Exception {
        Dish newDish = getNew();
        String json = JsonUtil.writeValue(newDish);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.getExisted(newId), newDish);
    }
}
