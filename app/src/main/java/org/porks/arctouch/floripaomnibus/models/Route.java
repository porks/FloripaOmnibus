package org.porks.arctouch.floripaomnibus.models;

/**
 * A route for the bus
 */
public class Route {
    /**
     * route's ID
     */
    private int id;

    /**
     * Short name for the route
     */
    private String shortName;

    /**
     * Long name for the route
     */
    private String longName;

    public Route(int id, String shortName, String longName) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
    }

    public int getId() {
        return this.id;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getLongName() {
        return this.longName;
    }
}
