package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Parameter;
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
import ru.javaops.bootjava.app.AuthUtil;
import ru.javaops.bootjava.voting.util.VoteUtil;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.VoteRepository;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@Tag(name = "Vote", description = "API для голосования")
@Slf4j
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    static final String REST_URL = "/api/votes";

    static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    private final LocalDate VOTE_DATE = LocalDate.now();

    private boolean skipTimeLimit = false;

    @Autowired
    protected VoteRepository repository;

    @Autowired
    private Environment env;

    @PostConstruct
    private void init() {
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            skipTimeLimit = true;
        }
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return repository.findById(id).orElseThrow();
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

        if (LocalTime.now().isBefore(TIME_LIMIT) || skipTimeLimit) {
            Vote vote = repository.getByUserIdAndRestaurantId(userId, restaurantId, VOTE_DATE);
            if (vote == null) {
                vote = VoteUtil.createNew(userId, restaurantId);
            }
            vote.setCreated(LocalDate.now());
            repository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
            return ResponseEntity.created(uriOfNewResource).body(vote);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
