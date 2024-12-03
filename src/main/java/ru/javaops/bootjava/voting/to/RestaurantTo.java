package ru.javaops.bootjava.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.bootjava.common.to.NamedTo;
import ru.javaops.bootjava.voting.model.Restaurant;
import ru.javaops.bootjava.voting.model.RestaurantWithRating;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements RestaurantWithRating {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer rating;

    public RestaurantTo(Integer id, String name, Integer rating) {
        super(id, name);
        this.rating = rating;
    }

    public RestaurantTo(Restaurant restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getVotes().size());
    }

    public RestaurantTo(RestaurantWithRating restaurant) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getRating());
    }
}
