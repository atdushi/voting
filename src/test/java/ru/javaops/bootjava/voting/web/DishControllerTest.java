package ru.javaops.bootjava.voting.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.AbstractControllerTest;
import ru.javaops.bootjava.common.util.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.user.UserTestData.USER_MAIL;
import static ru.javaops.bootjava.voting.DishTestData.*;
import static ru.javaops.bootjava.voting.RestaurantTestData.TOKYO_CITY_ID;
import static ru.javaops.bootjava.voting.web.DishController.REST_URL;

public class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    private static final LocalDate DATE = LocalDate.of(2020, 1, 30);

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + LASAGNA_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant?restaurantId=" + TOKYO_CITY_ID + "&date=" + DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(lasagna1, lasagna2, lasagna3, lasagna4));

//        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-restaurant?restaurantId=" + TOKYO_CITY_ID))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        List list = JsonUtil.readValue(content, List.class);
//        Assertions.assertThat(list.size()).isEqualTo(4);

    }
}
