package org.porks.arctouch.floripaomnibus.actdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.porks.arctouch.floripaomnibus.R;
import org.porks.arctouch.floripaomnibus.models.RouteStop;

/**
 * The Tab showing the route stops
 */
public class TabRoute extends Fragment {
    /**
     * Reference for the List View
     */
    private ListView lsvStops = null;

    /**
     * JSON Object with the route's stops
     */
    private JSONObject stopsJSON = null;

    public TabRoute() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.viewpage_route, container, false);

        // Reference for the List View
        synchronized (TabRoute.this) {
            this.lsvStops = (ListView) view.findViewById(R.id.ViewPageRoute_LsvStops);
        }

        JSONObject stopsJSON = null;
        try {
            if (savedInstanceState != null) {
                // Restore the saved GUI
                String aux = savedInstanceState.getString("stopsJSON");
                if (aux != null)
                    stopsJSON = new JSONObject(aux);
            }
        } catch (Exception ex) {
            Toast.makeText(TabRoute.this.getActivity(), "Error restoring the route info", Toast.LENGTH_LONG).show();
        }

        // If already have the stops information, update de GUI
        this.updateStops(stopsJSON);

        // Return the Fragment's Layout
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("stopsJSON", this.stopsJSON.toString());
    }

    /**
     * Update the Stops List View based on the JSON
     */
    public synchronized void updateStops(JSONObject _stopsJSON) {
        try {
            // This method can be called when the GUI is ready
            // Or When the WS respond with the Stops
            if (_stopsJSON != null)
                this.stopsJSON = _stopsJSON;
            else if (this.stopsJSON == null)
                return;

            // Anyway the GUI might not be ready
            if (this.lsvStops == null)
                return;

            // Create the list
            final java.util.List<RouteStop> listRouteStops = new java.util.ArrayList<>();

            JSONArray rows = this.stopsJSON.getJSONArray("rows");
            for (int index = 0; index < rows.length(); index++) {
                JSONObject stop = rows.getJSONObject(index);
                int id = stop.getInt("id");
                String name = stop.getString("name");
                int sequence = stop.getInt("sequence");
                listRouteStops.add(new RouteStop(id, name, sequence));
            }

            // Set the list in the ListView
            TabRoute.this.lsvStops.setAdapter(//
                    new ArrayAdapter<RouteStop>(TabRoute.this.getActivity(), R.layout.listview_routestops, listRouteStops) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View row;
                            if (convertView == null) {
                                LayoutInflater inflater = (LayoutInflater) TabRoute.this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                row = inflater.inflate(R.layout.listview_routestops, parent);
                            } else {
                                row = convertView;
                            }
                            RouteStop stop = listRouteStops.get(position);
                            ((TextView) row.findViewById(R.id.ListViewRouteStops_Text1)).setText(Integer.toString(stop.getSequence()));
                            ((TextView) row.findViewById(R.id.ListViewRouteStops_Text2)).setText(stop.getName());

                            return row;
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(TabRoute.this.getActivity(), "Problem updating the Stops List", Toast.LENGTH_LONG).show();
        }
    }
}
