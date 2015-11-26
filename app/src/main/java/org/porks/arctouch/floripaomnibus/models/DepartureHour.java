package org.porks.arctouch.floripaomnibus.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A hour of the day with all the Departures of this hour
 */
public class DepartureHour {
    /**
     * Hour of the day
     */
    private String hourName;

    /**
     * List with the Departures of the hour
     */
    private List<String> listDeparture = new ArrayList<>();

    public DepartureHour(String hourName) {
        this.hourName = hourName;
    }

    public String getHourName() {
        return this.hourName;
    }

    public List<String> getListDeparture() {
        return this.listDeparture;
    }
}
