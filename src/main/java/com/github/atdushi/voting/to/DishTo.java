package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.voting.util.View;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.github.atdushi.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @NotNull
    @JsonView(View.CreateRead.class)    // can't update restaurantId
    Integer restaurantId;

    @NotNull
    Integer price;

    public DishTo(Integer id, String name, Integer price, Integer restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }
}
