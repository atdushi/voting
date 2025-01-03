package com.github.atdushi.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import com.github.atdushi.common.to.BaseTo;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    Integer userId;
    Integer restaurantId;
    LocalDate date;

    public VoteTo(Integer id, Integer userId, Integer restaurantId, LocalDate date) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.date = date;
    }
}
