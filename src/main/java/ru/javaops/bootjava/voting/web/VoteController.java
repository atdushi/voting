package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.app.AuthUtil;
import ru.javaops.bootjava.voting.VoteUtil;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.VoteRepository;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

@Tag(name = "Vote", description = "API для голосования")
@Slf4j
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    static final String REST_URL = "/api/vote";

    static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    private final LocalDate VOTE_DATE = LocalDate.now();

    @Autowired
    protected VoteRepository repository;

    @GetMapping("/{voteId}")
    public Vote get(@PathVariable int voteId) {
        return repository.findById(voteId).orElseThrow();
    }

    @Operation(summary = "only votes before 11 A.M. are count")
    @Parameters({
        @Parameter(name = "restaurantId", description = "id ресторана")
    })
    @PostMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@PathVariable int restaurantId) {
        int userId = AuthUtil.get().id();
        log.info("vote restaurant {} user {}", restaurantId, userId);

        if (!LocalTime.now().isBefore(TIME_LIMIT)) {
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
