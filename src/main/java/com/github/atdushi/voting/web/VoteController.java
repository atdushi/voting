package com.github.atdushi.voting.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.app.AuthUtil;
import com.github.atdushi.common.error.IllegalRequestDataException;
import com.github.atdushi.common.error.NotFoundException;
import com.github.atdushi.user.model.User;
import com.github.atdushi.voting.View;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.atdushi.voting.util.VoteUtil.TIME_LIMIT;

@Tag(name = "Vote", description = "API для голосования")
@Slf4j
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class VoteController {

    static final String REST_URL = "/api/votes";

    @Value("${voting.skipTimeCheck}")
    private boolean skipTimeCheck;  // for testing purposes

    @Autowired
    private final VoteRepository voteRepository;

    @Autowired
    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "информация о голосе")
    @Parameters({
            @Parameter(name = "id", description = "id голоса")
    })
    @GetMapping("/{id}")
    @Transactional
    public VoteTo get(@PathVariable int id) {
        return VoteUtil.getTo(voteRepository.getExisted(id));
    }

    @Operation(summary = "посмотреть свой голос за сегодня")
    @GetMapping("/today")
    @Transactional
    @JsonView(View.Web.class)
    public ResponseEntity<VoteTo> getToday() {
        User user = AuthUtil.get().getUser();
        log.info("get today's vote of user {}", user.getId());

        Optional<Vote> vote = voteRepository.getByUserAndDate(user, getVotingDate());
        return vote.map(value -> ResponseEntity.ok(VoteUtil.getTo(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "посмотреть историю своих голосов")
    @GetMapping("/history")
    @Transactional
    @JsonView(View.Web.class)
    public List<VoteTo> getVotingHistory() {
        User user = AuthUtil.get().getUser();
        log.info("get voting history of user {}", user.getId());

        List<Vote> votes = voteRepository.getByUserOrderByDateDesc(user);
        if (votes.isEmpty()) {
            throw new NotFoundException("Votes not found");
        }
        return VoteUtil.getTos(votes);
    }

    @Operation(summary = "количество голосов за ресторан")
    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана"),
            @Parameter(name = "date", description = "дата голосования (по умолчанию - текущая)")
    })
    @GetMapping("/count-by-restaurant")
    public long countByRestaurant(@RequestParam int restaurantId, @RequestParam Optional<LocalDate> date) {
        LocalDate voteDate = date.orElse(LocalDate.now());
        Restaurant existed = restaurantRepository.getExisted(restaurantId);
        return voteRepository.countByRestaurantAndDate(existed, voteDate);
    }

    @Operation(summary = "голосование за ресторан, повторные голоса учитываются только до 11:00")
    @Parameters({
            @Parameter(name = "restaurantId", description = "id ресторана")
    })
    @PostMapping(value = "/{restaurantId}")
    @Transactional
    public ResponseEntity<VoteTo> vote(@PathVariable int restaurantId) {
        User user = AuthUtil.get().getUser();
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        log.info("vote restaurant {} user {}", restaurant, user);

        LocalDate votingDate = getVotingDate();
        Optional<Vote> existed = voteRepository.getByUserAndDate(user, votingDate);

        if (existed.isEmpty()) {
            Vote vote = VoteUtil.createNew(user.id(), restaurantId);
            vote = voteRepository.save(vote);
            vote = voteRepository.getExisted(vote.id());

            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(vote.getId()).toUri();

            return ResponseEntity.created(uriOfNewResource).body(VoteUtil.getTo(vote));
        } else {
            // re-vote
            if (LocalTime.now().isBefore(TIME_LIMIT) || skipTimeCheck) {
                if (existed.get().getRestaurant().id() != restaurantId) {
                    existed.get().setRestaurant(restaurant);
                    voteRepository.save(existed.get());
                }
                return ResponseEntity.noContent().build();
            } else {
                throw new IllegalRequestDataException("Can't re-vote after " + TIME_LIMIT + " a.m.");
            }
        }
    }

    private static LocalDate getVotingDate() {
        return LocalDate.now();
    }
}
