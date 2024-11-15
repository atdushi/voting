package ru.javaops.bootjava.voting.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.voting.RestaurantUtil;
import ru.javaops.bootjava.voting.VoteUtil;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.model.Vote;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.repository.VoteRepository;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javaops.bootjava.common.validation.ValidationUtil.checkNew;

@Slf4j
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/restaurant";
    public static final LocalTime TIME_LIMIT = LocalTime.of(11, 0);

    @Autowired
    protected RestaurantRepository repository;

    @Autowired
    protected VoteRepository voteRepository;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return RestaurantUtil.createTo(repository.findById(id).orElseThrow());
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> register(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("register {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody @Valid RestaurantTo restaurantTo) {
        log.info("update {}", restaurantTo);
        repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
    }

    @PostMapping(value = "/{id}/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@PathVariable int id, @Valid @RequestBody Integer userId) {
        log.info("vote restaurant {} user {}", id, userId);

        if (LocalTime.now().isBefore(TIME_LIMIT)) {
            Vote vote = voteRepository.getByUserIdAndRestaurantId(userId, id);
            if (vote == null) {
                vote = VoteUtil.createNew(userId, id);
            }
            vote.setCreated(LocalDateTime.now());
            voteRepository.save(vote);
            return ResponseEntity.ok(vote);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
