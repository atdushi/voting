package ru.javaops.bootjava.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.voting.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id = ?1")
    int delete(int id);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = ?1 ORDER BY d.name")
    List<Dish> getAll(int restaurantId);
}
