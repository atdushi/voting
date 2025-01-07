package com.github.atdushi.voting.repository;

import com.github.atdushi.voting.model.Restaurant;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.voting.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id = ?1")
    int delete(int id);

    List<Dish> getByRestaurantAndDateOrderByNameAsc(Restaurant restaurant, LocalDate date);

    @Cacheable("dishes")
    List<Dish> getByDateOrderByRestaurantNameAscNameAsc(LocalDate date);
}
