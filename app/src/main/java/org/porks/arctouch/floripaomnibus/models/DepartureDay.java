package org.porks.arctouch.floripaomnibus.models;

import java.util.TreeMap;

/**
 * A day of the week (and weekend) with all the Departures hours
 */
public class DepartureDay {
    /**
     * Name of the day
     */
    private String dayName;

    /**
     * List with the Departures grouped by the hour of the day
     */
    private TreeMap<String, DepartureHour> hashMapHours = new TreeMap<>();

    public DepartureDay(String dayName) {
        this.dayName = dayName;
    }

    public String getDayName() {
        return this.dayName;
    }

    public TreeMap<String, DepartureHour> getHashMapHours() {
        return this.hashMapHours;
    }

    @Override
    public String toString()
    {
        return this.dayName;
    }
}
