package ru.javaops.bootjava.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.bootjava.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {
    public RestaurantTo(Integer id, String name) {
        super(id, name);
    }
}
