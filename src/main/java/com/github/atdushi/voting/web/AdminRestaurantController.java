package com.github.atdushi.voting.web;

import com.github.atdushi.common.error.NotFoundException;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.repository.RestaurantRepository;
import com.github.atdushi.voting.to.RestaurantTo;
import com.github.atdushi.voting.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.github.atdushi.common.validation.ValidationUtil.assureIdConsistent;
import static com.github.atdushi.common.validation.ValidationUtil.checkNew;

@Tag(name = "Admin Restaurant", description = "API администратора для работы с ресторанами")
@Slf4j
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    private final RestaurantRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurants", "dishes", "restaurantWithDishes"}, allEntries = true)
    public void delete(@PathVariable int id) {
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = {"restaurants", "restaurantWithDishes"}, allEntries = true)
    public ResponseEntity<Restaurant> register(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("register {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "restaurantWithDishes", key = "#id")
    })
    public void update(@PathVariable int id, @Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("update restaurant with id={}", id);
        assureIdConsistent(restaurantTo, id);
        if (!repository.existsById(id)) {
            throw new NotFoundException("Restaurant with id=" + id + " not found");
        }
        repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurants", "restaurantWithDishes"}, allEntries = true)
    public void enable(@PathVariable int id, @RequestParam String name) {
        log.info("update restaurant {} with name {}", id, name);
        Restaurant restaurant = repository.getExisted(id);
        restaurant.setName(name);
        repository.save(restaurant);
    }
}
