package com.github.atdushi.voting.web;

import com.github.atdushi.app.AuthUtil;
import com.github.atdushi.common.error.IllegalRequestDataException;
import com.github.atdushi.common.error.NotFoundException;
import com.github.atdushi.user.model.User;
import com.github.atdushi.voting.model.Restaurant;
import com.github.atdushi.voting.model.Vote;
import com.github.atdushi.voting.repository.RestaurantRepository;
import com.github.atdushi.voting.repository.VoteRepository;
import com.github.atdushi.voting.to.VoteTo;
import com.github.atdushi.voting.util.VoteUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.github.atdushi.voting.util.VoteUtil.TIME_LIMIT;

@Tag(name = "Vote", description = "API для голосования")
@Slf4j
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class VoteController {

    static final String REST_URL = "/api/votes";

    @Autowired
    private final VoteRepository voteRepository;

    @Autowired
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        return VoteUtil.getTo(voteRepository.getExisted(id));
    }

    @Operation(summary = "голоса текущего пользователя за дату (по-умолчанию - текущая)")
    @GetMapping
    public VoteTo getVotesOfUser(@RequestParam(required = false) Optional<LocalDate> date) {
        LocalDate voteDate = date.orElse(LocalDate.now());
        User user = AuthUtil.get().getUser();
        log.info("get votes for user {} date {}", user.getId(), voteDate);
        Optional<Vote> vote = voteRepository.getByUserAndDate(user, voteDate);
        if (vote.isEmpty()) {
            throw new NotFoundException("Vote not found");
        }
        return VoteUtil.getTo(vote.get());
    }

    @GetMapping("/count-by-restaurant")
    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана"),
            @Parameter(name = "date", description = "дата голосования (по-умолчанию - текущая)")
    })
    public long countByRestaurant(@RequestParam int restaurantId, @RequestParam(required = false) Optional<LocalDate> date) {
        LocalDate voteDate = date.orElse(LocalDate.now());
        Restaurant existed = restaurantRepository.getExisted(restaurantId);
        return voteRepository.countByRestaurantAndDate(existed, voteDate);
    }

    @Operation(summary = "голосование за ресторан, повторные голоса учитываются только до 11:00")
    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана")
    })
    @PostMapping(value = "/{restaurantId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@PathVariable int restaurantId) {
        User user = AuthUtil.get().getUser();
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        log.info("vote restaurant {} user {}", restaurant, user);

        LocalDate votingDate = getVotingDate();
        Optional<Vote> existed = voteRepository.getByUserAndDate(user, votingDate);

        if (existed.isEmpty()) {
            Vote vote = VoteUtil.createNew(user.id(), restaurantId);
            voteRepository.save(vote);

            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(vote.getId()).toUri();

            return ResponseEntity.created(uriOfNewResource).body(vote);
        } else {
            // re-vote
            if (LocalTime.now().isBefore(TIME_LIMIT)) {
                if (existed.get().getRestaurant().id() != restaurantId) {
                    existed.get().setRestaurant(restaurant);
                    voteRepository.save(existed.get());
                }
                return ResponseEntity.ok(existed.get());
            } else {
                throw new IllegalRequestDataException("Can't re-vote after " + TIME_LIMIT + " a.m.");
            }
        }
    }

    private static LocalDate getVotingDate() {
        return LocalDate.now();
    }
}
