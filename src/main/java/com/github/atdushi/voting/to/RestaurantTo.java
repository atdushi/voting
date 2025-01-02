package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.github.atdushi.common.to.NamedTo;
import com.github.atdushi.voting.model.RestaurantWithRating;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements RestaurantWithRating {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(Include.NON_NULL)
    Integer rating;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(Include.NON_NULL)
    List<DishTo> dishes;

    public RestaurantTo(Integer id, String name, List<DishTo> dishes, Integer rating) {
        super(id, name);
        this.rating = rating;
        this.dishes = dishes;
    }
}
