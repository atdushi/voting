package ru.javaops.bootjava.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.voting.model.Restaurant;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = ?1")
    int delete(int id);

    @Query("""
             select r
                from Vote v
                join Restaurant r on v.restaurant.id = r.id
                group by r
                order by count(*) desc
            """)
    // limit 1
    Restaurant findFirstByVotesCountDesc();

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.dishes")
    List<Restaurant> findAllWithDishes();
}
