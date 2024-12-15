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
import ru.javaops.bootjava.common.validation.ValidationUtil;
import ru.javaops.bootjava.voting.util.DishUtil;
import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.repository.DishRepository;
import ru.javaops.bootjava.voting.to.DishTo;

import java.net.URI;

import static ru.javaops.bootjava.common.validation.ValidationUtil.*;

@Tag(name = "Admin Dish", description = "API администратора для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    static final String REST_URL = "/api/admin/dishes";

    @Autowired
    protected DishRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurantDishes", allEntries = true)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurantDishes", allEntries = true)
    public ResponseEntity<String> update(@PathVariable int id, @Valid @RequestBody DishTo dishTo, BindingResult result) {
        if (result.hasErrors()) {
            return ValidationUtil.getErrorResponse(result);
        }
        log.info("update {}", dishTo);
        assureIdConsistent(dishTo, id);
        Dish existed = repository.getExisted(id);
        Dish newFromTo = DishUtil.createNewFromTo(dishTo);
        newFromTo.setDate(existed.getDate());
        repository.save(newFromTo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "restaurantDishes", allEntries = true)
    public ResponseEntity<Dish> register(@Valid @RequestBody DishTo dishTo) {
        log.info("register {}", dishTo);
        checkNew(dishTo);
        Dish created = repository.save(DishUtil.createNewFromTo(dishTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL+ "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
