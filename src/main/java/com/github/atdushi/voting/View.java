package com.github.atdushi.voting;

/**
 * The View class contains nested interfaces used for grouping validation constraints.
 */
public class View {
    /**
     * The Create interface is used to group validation constraints for create operations.
     */
    public interface Create {}

    /**
     * The Update interface is used to group validation constraints for update operations.
     */
    public interface Update {}

    /**
     * The Web interface is used to group validation constraints for view operations.
     */
    public interface Web {}
}
