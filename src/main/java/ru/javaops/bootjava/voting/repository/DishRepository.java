package ru.javaops.bootjava.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.common.BaseRepository;
import ru.javaops.bootjava.voting.model.Dish;
import ru.javaops.bootjava.voting.model.Restaurant;

import java.sql.Date;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id = ?1")
    int delete(int id);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = ?1 AND d.date = ?2 ORDER BY d.name")
    List<Dish> getAllByRestaurantId(int restaurantId, Date date);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :#{#restaurant.id()} AND d.date = :date ORDER BY d.name")
    List<Dish> getAllByRestaurant(
            @Param("restaurant") Restaurant restaurant,
            @Param("date") Date date);
}
