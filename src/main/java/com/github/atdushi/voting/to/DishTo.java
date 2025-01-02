package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.common.to.NamedTo;
import com.github.atdushi.voting.util.View;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @JsonView(View.CreateRead.class)    // can't update
    Integer restaurantId;

    @NotNull
    Integer price;

    @NotNull
    LocalDate date;

    public DishTo(Integer id, String name, Integer price, Integer restaurantId, LocalDate date) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
        this.date = date;
    }
}
