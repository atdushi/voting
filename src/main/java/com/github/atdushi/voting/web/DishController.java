package com.github.atdushi.voting.web;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.atdushi.voting.util.DishUtil;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.repository.DishRepository;
import com.github.atdushi.voting.to.DishTo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Dish", description = "API для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    static final String REST_URL = "/api/dishes";

    @Autowired
    protected DishRepository repository;

    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана"),
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping("/by-restaurant")
    public List<DishTo> getAllByRestaurant(@RequestParam int restaurantId, @RequestParam(required = false) LocalDate date) {
        log.info("getAll for restaurant {}", restaurantId);
        List<Dish> all = repository.getAllByRestaurantId(restaurantId, date == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(date));
        return DishUtil.getTos(all);
    }

    @GetMapping()
    public List<DishTo> getAll() {
        log.info("getAll");
        List<Dish> all = repository.findAll();
        return DishUtil.getTos(all);
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int id) {
        return DishUtil.getTo(repository.findById(id).orElseThrow());
    }
}
