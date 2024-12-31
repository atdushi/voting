package com.github.atdushi.voting.model;

import com.github.atdushi.common.HasId;

public interface RestaurantWithRating extends HasId {
    String getName();
    Integer getRating();
}
