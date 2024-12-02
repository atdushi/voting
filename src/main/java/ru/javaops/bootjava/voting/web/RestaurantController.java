package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.voting.RestaurantUtil;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/api/restaurant";

    @Autowired
    protected RestaurantRepository repository;

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
}
