package org.porks.arctouch.floripaomnibus.models;

/**
 * A Stop in the route for the bus
 */
public class RouteStop {
    /**
     * stop ID
     */
    private final int id;

    /**
     * Name for the stop (street name)
     */
    private final String name;

    /**
     * Sequence of the Stop inside the route
     */
    private final int sequence;

    public RouteStop(int id, String name, int sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getSequence() {
        return this.sequence;
    }
}
