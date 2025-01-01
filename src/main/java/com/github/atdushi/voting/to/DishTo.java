package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.github.atdushi.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Integer restaurantId;

    @NotNull
    Double price;

    public DishTo(Integer id, String name, Double price, Integer restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }
}
