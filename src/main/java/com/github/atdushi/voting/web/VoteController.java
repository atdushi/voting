package com.github.atdushi.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.atdushi.app.AuthUtil;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.repository.VoteRepository;
import com.github.atdushi.voting.to.VoteTo;
import com.github.atdushi.voting.util.VoteUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Tag(name = "Vote", description = "API для голосования")
@Slf4j
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    static final String REST_URL = "/api/votes";

    static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    // skip time check for testing purposes
    private boolean skipTimeCheck = false;

    @Autowired
    private VoteRepository repository;

    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            skipTimeCheck = true;
        }
    }

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        return VoteUtil.getTo(repository.findById(id).orElseThrow());
    }

    @GetMapping("/count-by-restaurant")
    public int countByRestaurant(@RequestParam int restaurantId) {
        List<Vote> votes = repository.getByRestaurantId(restaurantId, getVotingDate());
        return votes.size();
    }

    @Operation(summary = "учитываются голоса только до 11:00")
    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@RequestBody int restaurantId) {
        int userId = AuthUtil.get().id();
        log.info("vote restaurant {} user {}", restaurantId, userId);

        if (LocalTime.now().isBefore(TIME_LIMIT) || skipTimeCheck) {
            List<Vote> votes = repository.getByUserIdAndRestaurantId(userId, restaurantId, getVotingDate());
            Vote vote = !votes.isEmpty() ? votes.getFirst() : null;
            if (vote == null) {
                vote = VoteUtil.createNew(userId, restaurantId);
            }
            repository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(vote.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(vote);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    private static LocalDate getVotingDate(){
        return LocalDate.now();
    }
}
