package com.github.atdushi.voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.RestaurantWithRating;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = ?1")
    int delete(int id);

    @Query(value = """
            select r.id, r.name, coalesce(cast(sum(v1.cnt) as int), 0) rating
            from restaurant r
            left join (select v.*, 1 cnt from vote v where v.vote_date = :date) v1 on v1.restaurant_id = r.id
            group by r.id, r.name
            order by count(*) desc, r.name asc
            """, nativeQuery = true)
    List<RestaurantWithRating> findAllByRatingDesc(LocalDate date);

    @Query("""
            SELECT r
            FROM Restaurant r
            LEFT JOIN FETCH r.dishes d
            WHERE r.id = :id and d.date = :date""")
    Optional<Restaurant> findWithDishes(int id, LocalDate date);
}
