package com.github.atdushi.voting.web;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.atdushi.voting.util.DishUtil;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.repository.DishRepository;
import com.github.atdushi.voting.to.DishTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Dish", description = "API для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    static final String REST_URL = "/api/dishes";

    @Autowired
    private DishRepository repository;

    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана"),
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping
    public List<DishTo> getByRestaurant(@RequestParam int restaurantId, @RequestParam(required = false) Optional<LocalDate> date) {
        log.info("get all by restaurant {}", restaurantId);
        LocalDate dishDate = date.orElse(LocalDate.now());
        List<Dish> all = repository.getByRestaurantId(restaurantId, dishDate);
        return DishUtil.getTos(all);
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int id) {
        log.info("get dish {}", id);
        return DishUtil.getTo(repository.findById(id).orElseThrow());
    }

    @Parameters({
            @Parameter(name = "date", description = "дата еды в ресторанах (по умолчанию - текущая)")
    })
    @Cacheable("dishes")
    @GetMapping("/by-date")
    public List<DishTo> getByDate(@RequestParam(required = false) Optional<LocalDate> date) {
        LocalDate dishDate = date.orElse(LocalDate.now());
        log.info("get dishes for date {}", date);
        List<Dish> byDate = repository.getByDate(dishDate);
        return DishUtil.getTos(byDate);
    }
}
