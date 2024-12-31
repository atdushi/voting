package com.github.atdushi.voting.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.RestaurantWithRating;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id = ?1")
    int delete(int id);

    @Query("""
            SELECT r
            FROM Vote v
            JOIN Restaurant r ON v.restaurant.id = r.id
            WHERE v.created = :date
            GROUP BY r
            ORDER BY COUNT(*) DESC
            """)
    Restaurant findFirstByRatingDesc(LocalDate date);   // limit 1

    @Cacheable("restaurantsWithRating")
    @Query(value = """
            select r.id, r.name, cast(sum(v1.cnt) as int) rating
            from restaurant r
            left join (select v.*, 1 cnt from vote v where v.created = :date) v1 on v1.restaurant_id = r.id
            group by r.id, r.name
            order by count(*) desc
            """, nativeQuery = true)
    List<RestaurantWithRating> findAllByRatingDesc(Date date);

    @Cacheable("restaurants")
    @Query("""
            SELECT r
            FROM Restaurant r
            LEFT JOIN FETCH r.dishes d
            LEFT JOIN FETCH r.votes v
            WHERE v.created = :date OR v.created IS NULL""")
    List<Restaurant> findAllWithDishesAndVotes(LocalDate date);
}
