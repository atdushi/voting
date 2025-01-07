package com.github.atdushi.voting.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.atdushi.common.HasId;
import com.github.atdushi.voting.to.RestaurantTo;

@JsonDeserialize(as = RestaurantTo.class)   // so Jackson will use RestaurantTo for deserialization
public interface RestaurantWithRating extends HasId {
    String getName();
    Integer getRating();
}
