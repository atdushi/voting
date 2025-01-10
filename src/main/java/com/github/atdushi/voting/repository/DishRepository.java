package com.github.atdushi.voting.repository;

import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.voting.model.Dish;
import com.github.atdushi.voting.model.Restaurant;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    List<Dish> getByRestaurantAndDateOrderByNameAsc(Restaurant restaurant, LocalDate date);

    @Cacheable("dishes")
    List<Dish> getByDateOrderByRestaurantNameAscNameAsc(LocalDate date);
}
