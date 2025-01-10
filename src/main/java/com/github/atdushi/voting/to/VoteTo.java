package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.common.to.BaseTo;
import com.github.atdushi.voting.View;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @Positive
    @JsonView(View.Create.class)
    int userId;

    @Positive
    int restaurantId;

    LocalDate date;

    public VoteTo(Integer id, int userId, int restaurantId, LocalDate date) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
    }
}
