package ru.javaops.bootjava.voting.to;

import ru.javaops.bootjava.common.HasId;

import java.util.List;

public interface RestaurantTo extends HasId {

    String getName();

    List<DishTo> getDishes();

    Integer getRating();
}
