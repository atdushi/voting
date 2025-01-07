package com.github.atdushi.voting.web;

import com.github.atdushi.common.error.NotFoundException;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.RestaurantWithRating;
import com.github.atdushi.voting.repository.RestaurantRepository;
import com.github.atdushi.voting.to.RestaurantTo;
import com.github.atdushi.voting.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Restaurant", description = "API для работы с ресторанами")
@Slf4j
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    @Autowired
    private final RestaurantRepository repository;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get by id {}", id);
        Restaurant restaurant = repository.getExisted(id);
        return RestaurantUtil.getTo(restaurant);
    }

    @Operation(summary = "ресторан с меню на дату")
    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана"),
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping("/with-dishes")
    public RestaurantTo get(@RequestParam int restaurantId, @RequestParam(required = false) Optional<LocalDate> date) {
        LocalDate dishDate = date.orElse(LocalDate.now());
        log.info("get with dishes by id {}", restaurantId);
        Optional<Restaurant> withDishes = repository.findWithDishes(restaurantId, dishDate);
        if (withDishes.isEmpty()) {
            throw new NotFoundException("Restaurant with id=" + restaurantId + " not found");
        }
        return RestaurantUtil.getTo(withDishes.get(), true);
    }


    @Cacheable("restaurants")
    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        List<Restaurant> all = repository.findAll();
        return RestaurantUtil.getTos(all, false);
    }

    @Parameters({
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping("/order-by-rating-desc")
    public List<RestaurantTo> getAllByRating(@RequestParam(required = false) Optional<LocalDate> date) {
        LocalDate votingDate = date.orElse(LocalDate.now());
        log.info("getAll by rating desc on date {}", votingDate);
        List<RestaurantWithRating> all = repository.findAllByRatingDesc(votingDate);
        return RestaurantUtil.getTos(all);
    }
}
