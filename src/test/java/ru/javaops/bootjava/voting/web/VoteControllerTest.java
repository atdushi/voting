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

public class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TOKYO_VOTE_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(TokyoVote1));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createNew() throws Exception {
        Vote newVote = getNew();
        String json = JsonUtil.writeValue(VOTE_DATE);

        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + newVote.getRestaurant().id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getExisted(newId), newVote);
    }
}
