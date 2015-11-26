package org.porks.arctouch.floripaomnibus.actdetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.porks.arctouch.floripaomnibus.R;
import org.porks.arctouch.floripaomnibus.models.DepartureDay;
import org.porks.arctouch.floripaomnibus.models.DepartureHour;

import java.util.TreeMap;

/**
 * The Tab showing the bus' Departures
 */
public class TabDeparture extends Fragment {
    /**
     * JSON Object with the route's Departures
     */
    private JSONObject departureJSON = null;

    /**
     * Spinner to choose the day of the Departures
     */
    private Spinner spnDays = null;

    /**
     * Expandable List View for the Hours and Departures of the Day
     */
    private ExpandableListView lsvDepartureHours = null;

    public TabDeparture() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.viewpage_departure, container, false);

        // Reference for the List View
        synchronized (TabDeparture.this) {
            this.spnDays = (Spinner) view.findViewById(R.id.ViewPageDeparture_SpnDays);
            this.lsvDepartureHours = (ExpandableListView) view.findViewById(R.id.ViewPageDeparture_LsvDeparture);
        }

        JSONObject departureJSON = null;
        try {
            if (savedInstanceState != null) {
                // Restore the saved GUI
                String aux = savedInstanceState.getString("departureJSON");
                if (aux != null)
                    departureJSON = new JSONObject(aux);
            }
        } catch (Exception ex) {
            Toast.makeText(TabDeparture.this.getActivity(), "Error restoring the route Departures", Toast.LENGTH_LONG).show();
        }

        // If already have the departures information, update de GUI
        this.updateDeparture(departureJSON);

        // Return the Fragment's Layout
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("departureJSON", this.departureJSON.toString());
    }

    /**
     * Update the Departure List View based on the JSON
     */
    public synchronized void updateDeparture(JSONObject _departureJSON) {
        try {
            // This method can be called when the GUI is ready
            // Or When the WS respond with the Departures
            if (_departureJSON != null)
                this.departureJSON = _departureJSON;
            else if (this.departureJSON == null)
                return;

            // Anyway the GUI might not be ready
            if (this.spnDays == null)
                return;

            // Create the list
            final TreeMap<String, DepartureDay> hashDepartureDays = new TreeMap<>();
            DepartureDay day = null;
            DepartureHour hour = null;

            JSONArray rows = this.departureJSON.getJSONArray("rows");
            for (int index = 0; index < rows.length(); index++) {
                JSONObject departure = rows.getJSONObject(index);
                int id = departure.getInt("id");
                String calendar = departure.getString("calendar");
                String time = departure.getString("time");

                // New day
                if ((day == null) || (!day.getDayName().equals(calendar))) {
                    // Test if the day exists in the HashMap
                    day = hashDepartureDays.get(calendar);
                    if (day == null) {
                        // Create the new day and add to the HashMap
                        day = new DepartureDay(calendar);
                        hashDepartureDays.put(calendar, day);
                    }
                }

                // New Hour
                String timeHour = "ERR";
                if (time.length() == 5)
                    timeHour = time.substring(0, 2);
                if ((hour == null) || (!hour.getHourName().equals(timeHour))) {
                    // Teste if the hour exists in the HashMap
                    hour = day.getHashMapHours().get(timeHour);
                    if (hour == null) {
                        // Create the new hour and add to the HashMap
                        hour = new DepartureHour(timeHour);
                        day.getHashMapHours().put(timeHour, hour);
                    }
                }

                // Add the Departure
                hour.getListDeparture().add(time);
            }

            // Build an Array from the HashMap
            final DepartureDay[] arrayDepartureDays = hashDepartureDays.values().toArray(new DepartureDay[0]);

            // Set the Spinner Adapter
            this.spnDays.setAdapter(new ArrayAdapter<DepartureDay>(TabDeparture.this.getActivity(), R.layout.spinner_item, arrayDepartureDays));
            this.spnDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Item Selected in the Spinner, let's build the ListView with the Day's Departures
                    try {
                        // Build an Array from the HashMap
                        DepartureDay day = arrayDepartureDays[position];
                        final DepartureHour[] arrayDepartureHours = day.getHashMapHours().values().toArray(new DepartureHour[0]);


                        // Set the list in the ListView
                        TabDeparture.this.lsvDepartureHours.setAdapter(new ExpandableListAdapter(arrayDepartureHours));

                    } catch (Exception ex) {
                        Toast.makeText(TabDeparture.this.getActivity(), "Problem updating the Day's Departures List", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception ex) {
            Toast.makeText(TabDeparture.this.getActivity(), "Problem updating the Departures List", Toast.LENGTH_LONG).show();
        }
    }
}
