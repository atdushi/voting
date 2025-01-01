package com.github.atdushi.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import com.github.atdushi.common.to.BaseTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    Integer userId;
    Integer restaurantId;

    public VoteTo(Integer id, Integer userId, Integer restaurantId) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
