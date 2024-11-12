package ru.javaops.bootjava.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.bootjava.common.to.NamedTo;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedTo {
    public DishTo(Integer id, String name) {
        super(id, name);
    }
}
