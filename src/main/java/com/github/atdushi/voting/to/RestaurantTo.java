package com.github.atdushi.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.atdushi.common.to.NamedTo;
import com.github.atdushi.voting.model.RestaurantWithRating;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements RestaurantWithRating {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer rating;

    public RestaurantTo(Integer id, String name, Integer rating) {
        super(id, name);
        this.rating = rating;
    }
}
