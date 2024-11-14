package ru.javaops.bootjava.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.user.model.User;
import ru.javaops.bootjava.voting.model.Vote;

import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id = :#{#user.id()}")
    int delete(@Param("user") User user);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :#{#restaurant.id()}")
    List<Vote> getAll(@Param("restaurant") Restaurant restaurant);

}