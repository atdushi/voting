package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.voting.RestaurantUtil;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.to.RestaurantTo;
import ru.javaops.bootjava.voting.model.RestaurantWithRating;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Restaurant", description = "API для работы с ресторанами")
@Slf4j
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    @Autowired
    protected RestaurantRepository repository;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        return new RestaurantTo(repository.findById(id).orElseThrow());
    }

    @GetMapping
    public List<RestaurantTo> getAll(@RequestParam(required = false) LocalDate date) {
        log.info("getAll");
        List<Restaurant> withDishesAndVotes = repository.findAllWithDishesAndVotes(date == null ? LocalDate.now() : date);
        return RestaurantUtil.getTos(withDishesAndVotes);
    }

    @Parameters({
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping("/order-by-rating-desc")
    public List<RestaurantTo> getAllByRating(@RequestParam(required = false) LocalDate date) {
        log.info("getAll by rating");
        List<RestaurantWithRating> all = repository.findAllByRatingDesc(date == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(date));
        return RestaurantUtil.getTos(all);
    }

    @Parameters({
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping("/top-ranked")
    @Transactional
    public RestaurantTo getTopRanked(@RequestParam(required = false) LocalDate date) {
        log.info("getTopRanked");
        Restaurant firstByRatingDesc = repository.findFirstByRatingDesc(date == null ? LocalDate.now() : date);
        return new RestaurantTo(firstByRatingDesc);
    }
}
