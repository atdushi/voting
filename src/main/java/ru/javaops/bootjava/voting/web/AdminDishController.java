package ru.javaops.bootjava.voting.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.voting.DishUtil;
import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.repository.DishRepository;
import ru.javaops.bootjava.voting.to.DishTo;

import java.net.URI;

import static ru.javaops.bootjava.common.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.bootjava.common.validation.ValidationUtil.checkNew;

@Tag(name = "Dish", description = "API для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    static final String REST_URL = "/api/admin/dish";

    @Autowired
    protected DishRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody DishTo dishTo) {
        log.info("update {}", dishTo);
        assureIdConsistent(dishTo, id);
        repository.save(DishUtil.createNewFromTo(dishTo));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> register(@Valid @RequestBody DishTo dishTo) {
        log.info("register {}", dishTo);
        checkNew(dishTo);
        Dish created = repository.save(DishUtil.createNewFromTo(dishTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
