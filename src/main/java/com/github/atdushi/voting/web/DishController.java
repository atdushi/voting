package com.github.atdushi.voting.web;

import com.github.atdushi.common.error.NotFoundException;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.repository.DishRepository;
import com.github.atdushi.voting.to.DishTo;
import com.github.atdushi.voting.util.DishUtil;
import com.github.atdushi.voting.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Dish", description = "API для работы с едой")
@Slf4j
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DishController {

    static final String REST_URL = "/api/dishes";

    @Autowired
    private final DishRepository repository;

    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана (по умолчанию - все)"),
            @Parameter(name = "date", description = "дата еды в ресторане (по умолчанию - текущая)")
    })
    @GetMapping
    @Transactional
    public List<DishTo> getByRestaurantAndDate(@RequestParam Optional<Integer> restaurantId,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> date) {
        log.info("get all by restaurant {} and date {}", restaurantId, date);
        LocalDate dishDate = date.orElse(LocalDate.now());
        if (restaurantId.isEmpty()) {
            List<Dish> all = repository.getByDateOrderByRestaurantNameAscNameAsc(dishDate);
            return getDishTos(all);
        }
        List<Dish> all = repository.getByRestaurantAndDateOrderByNameAsc(RestaurantUtil.createNewFromId(restaurantId.get()), dishDate);
        return getDishTos(all);
    }

    private static List<DishTo> getDishTos(List<Dish> all) {
        if (all.isEmpty()) {
            throw new NotFoundException("Dishes not found");
        }
        return DishUtil.getTos(all);
    }

    @GetMapping("/{id}")
    @Transactional
    public DishTo get(@PathVariable int id) {
        log.info("get dish {}", id);
        return DishUtil.getTo(repository.getExisted(id));
    }
}
