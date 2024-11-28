package ru.javaops.bootjava.voting.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.voting.DishUtil;
import ru.javaops.bootjava.voting.RestaurantUtil;
import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.repository.DishRepository;
import ru.javaops.bootjava.voting.to.DishTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.bootjava.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.bootjava.common.validation.ValidationUtil.checkNew;

@Slf4j
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/api/dish";

    @Autowired
    protected DishRepository repository;

    @GetMapping("/by-restaurant")
    public List<DishTo> getAllByRestaurant(@RequestParam int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        List<Dish> all = repository.getAllByRestaurant(RestaurantUtil.createNewFromId(restaurantId), LocalDate.of(2020, 1, 30));
        return DishUtil.getTos(all);
    }

    @GetMapping("/")
    public List<DishTo> getAll() {
        log.info("getAll");
        List<Dish> all = repository.findAll();
        return DishUtil.getTos(all);
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int id) {
        return DishUtil.createTo(repository.findById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Dish dish) {
        log.info("update {}", dish);
        assureIdConsistent(dish, id);
        repository.save(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> register(@Valid @RequestBody Dish dish) {
        log.info("register {}", dish);
        checkNew(dish);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
