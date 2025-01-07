package com.github.atdushi.voting.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.voting.util.View;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.atdushi.voting.util.DishUtil;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.repository.DishRepository;
import com.github.atdushi.voting.to.DishTo;

import java.net.URI;

import static com.github.atdushi.common.validation.ValidationUtil.*;

@Tag(name = "Admin Dish", description = "API администратора для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminDishController {

    static final String REST_URL = "/api/admin/dishes";

    @Autowired
    private final DishRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "dishes", allEntries = true)
    public void update(@PathVariable int id, @Validated(View.Update.class) @JsonView(View.Update.class) @RequestBody DishTo dishTo) {
        log.info("update {}", dishTo);
        assureIdConsistent(dishTo, id);
        Dish existed = repository.getExisted(id);
        Dish newFromTo = DishUtil.createNewFromTo(dishTo);
        newFromTo.setRestaurant(existed.getRestaurant());
        repository.save(newFromTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "dishes", allEntries = true)
    public ResponseEntity<Dish> register(@Validated(View.Create.class) @JsonView(View.Create.class) @RequestBody DishTo dishTo) {
        log.info("register {}", dishTo);
        checkNew(dishTo);
        Dish created = repository.save(DishUtil.createNewFromTo(dishTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL+ "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
