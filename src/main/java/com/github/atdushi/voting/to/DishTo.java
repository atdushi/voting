package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.atdushi.common.to.NamedTo;
import com.github.atdushi.voting.View;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @JsonView(View.Create.class)    // can't update
    int restaurantId;

    @Positive
    int price;

    @NotNull
    LocalDate date;

    public DishTo(Integer id, String name, int price, int restaurantId, LocalDate date) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
        this.date = date;
    }
}
