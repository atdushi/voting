package ru.javaops.bootjava.voting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.voting.DishUtil;
import ru.javaops.bootjava.voting.RestaurantUtil;
import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.repository.DishRepository;
import ru.javaops.bootjava.voting.to.DishTo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    static final String REST_URL = "/api/dish";

    @Autowired
    protected DishRepository repository;

    @GetMapping("/by-restaurant")
    public List<DishTo> getAllByRestaurant(@RequestParam int restaurantId, @RequestParam(required = false) LocalDate date) {
        log.info("getAll for restaurant {}", restaurantId);
        Restaurant newFromId = RestaurantUtil.createNewFromId(restaurantId);
        List<Dish> all = repository.getAllByRestaurant(newFromId, date == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(date));
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
        return DishUtil.createTo(repository.findById(id).orElseThrow());
    }
}
