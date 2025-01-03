package com.github.atdushi.voting.repository;

import com.github.atdushi.voting.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;
import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.user.model.User;
import com.github.atdushi.voting.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    long countByRestaurantAndDate(Restaurant restaurant, LocalDate date);

    Optional<Vote> getByUserAndDate(User user, LocalDate date);
}
