package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.common.error.IllegalRequestDataException;
import ru.javaops.bootjava.common.validation.ValidationUtil;
import ru.javaops.bootjava.voting.util.RestaurantUtil;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.repository.RestaurantRepository;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.net.URI;

import static ru.javaops.bootjava.common.validation.ValidationUtil.*;

@Tag(name = "Admin Restaurant", description = "API администратора для работы с ресторанами")
@Slf4j
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    protected RestaurantRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurantsWithRating", "restaurants"}, allEntries = true)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = {"restaurantsWithRating", "restaurants"}, allEntries = true)
    public ResponseEntity<Restaurant> register(@Valid @RequestBody RestaurantTo restaurantTo, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestDataException(ValidationUtil.getErrorResponse(result).toString());
        }
        log.info("register {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL+ "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restaurantsWithRating", "restaurants"}, allEntries = true)
    public void update(@PathVariable int id, @Valid @RequestBody RestaurantTo restaurantTo, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestDataException(ValidationUtil.getErrorResponse(result).toString());
        }
        log.info("update {}", restaurantTo);
        assureIdConsistent(restaurantTo, id);
        repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
    }
}
