package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.app.AuthUtil;
import ru.javaops.bootjava.app.Profiles;
import ru.javaops.bootjava.voting.VoteUtil;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Tag(name = "Vote", description = "API для голосования")
@Slf4j
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    static final String REST_URL = "/api/vote";

    public static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    private LocalDate VOTE_DATE = LocalDate.now();

    @Autowired
    protected VoteRepository voteRepository;

    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
        if (Arrays.stream(env.getActiveProfiles()).anyMatch(Profiles.DEVELOPMENT::equalsIgnoreCase)) {
            VOTE_DATE = LocalDate.of(2020, 1, 30);
        }
    }

    @Operation(summary = "only votes before 11 A.M. are count")
    @PostMapping(value = "/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@RequestParam int restaurantId) {
        int userId = AuthUtil.get().id();
        log.info("vote restaurant {} user {}", restaurantId, userId);

        if (!LocalTime.now().isBefore(TIME_LIMIT)) {
            Vote vote = voteRepository.getByUserIdAndRestaurantId(userId, restaurantId, VOTE_DATE);
            if (vote == null) {
                vote = VoteUtil.createNew(userId, restaurantId);
            }
            vote.setCreated(LocalDate.now());
            voteRepository.save(vote);
            return ResponseEntity.ok(vote);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
