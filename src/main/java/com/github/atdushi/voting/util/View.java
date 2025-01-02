package com.github.atdushi.voting.util;

/**
 * The View class contains nested interfaces used for grouping validation constraints.
 */
public class View {
    /**
     * The CreateRead interface is used to group validation constraints for create and read operations.
     */
    public interface CreateRead {}

    /**
     * The Update interface is used to group validation constraints for update operations.
     */
    public interface Update {}
}
