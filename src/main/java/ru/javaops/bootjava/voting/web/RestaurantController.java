package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.app.AuthUtil;
import ru.javaops.bootjava.app.Profiles;
import ru.javaops.bootjava.voting.RestaurantUtil;
import ru.javaops.bootjava.voting.VoteUtil;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.repository.VoteRepository;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/api/restaurant";

    public static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    private LocalDate VOTE_DATE = LocalDate.now();

    @Autowired
    protected RestaurantRepository repository;

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

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return RestaurantUtil.createTo(repository.findById(id).orElseThrow());
    }

    @Operation(summary = "get all with rating")
    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        List<Restaurant> withDishes = repository.findAllWithDishesAndVotes();
        return RestaurantUtil.getTos(withDishes, true, true);
    }

    @GetMapping("/order-by-rating-desc")
    public List<RestaurantTo> getAllByRating() {
        log.info("getAll by rating");
        List<RestaurantTo> all = repository.findAllByRatingDesc();
        return RestaurantUtil.unproxy(all);
    }

    @GetMapping("/top-ranked")
    @Transactional
    public RestaurantTo getTopRanked() {
        log.info("getTopRanked");
        Restaurant firstByRatingDesc = repository.findFirstByRatingDesc();
        return RestaurantUtil.createTo(firstByRatingDesc);
    }

    @PostMapping(value = "/{id}/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@PathVariable int id) {
        int userId = AuthUtil.get().id();
        log.info("vote restaurant {} user {}", id, userId);

        if (!LocalTime.now().isBefore(TIME_LIMIT)) {
            Vote vote = voteRepository.getByUserIdAndRestaurantId(userId, id, VOTE_DATE);
            if (vote == null) {
                vote = VoteUtil.createNew(userId, id);
            }
            vote.setCreated(LocalDate.now());
            voteRepository.save(vote);
            return ResponseEntity.ok(vote);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
