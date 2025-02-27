package com.github.atdushi.voting.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.voting.View;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.repository.DishRepository;
import com.github.atdushi.voting.to.DishTo;
import com.github.atdushi.voting.util.DishUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;

import static com.github.atdushi.common.validation.ValidationUtil.assureIdConsistent;
import static com.github.atdushi.common.validation.ValidationUtil.checkNew;

@Tag(name = "Admin Dish", description = "API администратора для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminDishController {

    static final String REST_URL = "/api/admin/dishes";

    @Autowired
    private final DishRepository repository;

    @Autowired
    private final CacheManager cacheManager;

    @PersistenceContext
    private EntityManager entityManager;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(@PathVariable int id) {
        repository.deleteExisted(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "dishes", allEntries = true)
    public void update(@PathVariable int id, @Valid @JsonView(View.Update.class) @RequestBody DishTo dishTo) {
        log.info("update {}", dishTo);
        assureIdConsistent(dishTo, id);

        Dish existed = repository.getExisted(id);
        Dish newFromTo = DishUtil.createNewFromTo(dishTo);

        Restaurant restaurant = existed.getRestaurant();

        newFromTo.setRestaurant(restaurant);
        repository.save(newFromTo);

        Cache restaurantWithDishes = cacheManager.getCache("restaurantWithDishes");
        Objects.requireNonNull(restaurantWithDishes).evict(Objects.requireNonNull(restaurant.getId()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public ResponseEntity<Dish> register(@Valid @RequestBody DishTo dishTo) {
        log.info("register {}", dishTo);
        checkNew(dishTo);

        Dish created = repository.save(DishUtil.createNewFromTo(dishTo));
        entityManager.refresh(created);             // refresh to bypass cache
        created.getRestaurant().setDishes(null);    // remove proxy

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"dishes", "restaurantWithDishes"}, allEntries = true)
    public void enable(@PathVariable int id,  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("update date {} for dish {}", date, id);
        Dish dish = repository.getExisted(id);
        dish.setDate(date);
        repository.save(dish);
    }
}
