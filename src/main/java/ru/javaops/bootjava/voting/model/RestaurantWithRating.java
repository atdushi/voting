package ru.javaops.bootjava.voting.model;

import ru.javaops.bootjava.common.HasId;

public interface RestaurantWithRating extends HasId {

    String getName();

    Integer getRating();
}
