package ru.javaops.bootjava.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.to.RestaurantTo;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = ?1")
    int delete(int id);

    @Query("""
             SELECT r
                FROM Vote v
                JOIN Restaurant r ON v.restaurant.id = r.id
                GROUP BY r
                ORDER BY COUNT(*) DESC
            """)
    // limit 1
    Restaurant findFirstByRatingDesc();

    @Query(value = """
             select r.id, r.name, cast(sum(v1.cnt) as int) rating
                from restaurant r
                left join (select v.*, 1 cnt from vote v) v1 on v1.restaurant_id = r.id
                group by r.id, r.name
                order by count(*) desc
            """, nativeQuery = true)
    List<RestaurantTo> findAllByRatingDesc();

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes LEFT JOIN FETCH r.votes")
    List<Restaurant> findAllWithDishesAndVotes();
}
