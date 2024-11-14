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
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.net.URI;
import java.util.List;

import static ru.javaops.bootjava.common.validation.ValidationUtil.checkNew;

@Slf4j
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/restaurant";

    @Autowired
    protected RestaurantRepository repository;

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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Restaurant restaurant) {
        repository.delete(restaurant);
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
}