package ru.javaops.bootjava.voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.common.BaseRepository;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.user.model.User;
import ru.javaops.bootjava.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id = :#{#user.id()}")
    int delete(@Param("user") User user);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :#{#restaurant.id()}")
    List<Vote> getAll(@Param("restaurant") Restaurant restaurant);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.restaurant.id = :restaurantId AND v.created = :created")
    Vote getByUserIdAndRestaurantId(
            @Param("userId") Integer userId,
            @Param("restaurantId") Integer restaurantId,
            @Param("created") LocalDate created);
}
