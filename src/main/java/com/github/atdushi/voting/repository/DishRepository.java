package com.github.atdushi.voting.repository;

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

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = ?1 AND d.date = ?2 ORDER BY d.name")
    List<Dish> getByRestaurantId(int restaurantId, LocalDate date);

    @Cacheable("dishes")
    @Query("SELECT d FROM Dish d WHERE d.date = ?1 ORDER BY d.restaurant.name, d.name")
    List<Dish> getByDate(LocalDate date);
}
