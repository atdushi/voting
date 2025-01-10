package com.github.atdushi.voting.repository;

import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.RestaurantWithRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query(value = """
            select r.id, r.name, coalesce(cast(sum(v1.cnt) as int), 0) rating
            from restaurant r
            left join (select v.*, 1 cnt from vote v where v.vote_date = :date) v1 on v1.restaurant_id = r.id
            group by r.id, r.name
            order by coalesce(cast(sum(v1.cnt) as int), 0) desc, r.name asc
            """, nativeQuery = true)
    List<RestaurantWithRating> findAllByRatingDesc(LocalDate date);

    Optional<Restaurant> findByIdAndDishesDate(int id, LocalDate date);
}
