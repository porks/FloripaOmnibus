package org.porks.arctouch.floripaomnibus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.porks.arctouch.floripaomnibus.actdetails.ActDetails;
import org.porks.arctouch.floripaomnibus.models.Route;
import org.porks.arctouch.floripaomnibus.wsaccess.TaskCallBack;
import org.porks.arctouch.floripaomnibus.wsaccess.TaskResult;
import org.porks.arctouch.floripaomnibus.wsaccess.WSParam;
import org.porks.arctouch.floripaomnibus.wsaccess.WSTask;

public class ActMain extends AppCompatActivity {

    /**
     * EditText where the user input the search text
     */
    private EditText edtSearch = null;

    /**
     * ListView with the result of the search
     */
    private ListView lsvResult = null;

    /**
     * Persist the JSON data when the Activity is recreated
     */
    private String routesJSON = null;

    /**
     * Value when we call the Map Activity to get the Street name from the Map
     */
    private static final int MAP_STREET_NAME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actmain);

        // Create the event onclick for the search and map buttons
        this.findViewById(R.id.ActMain_BtnSearch).setOnClickListener(this.btnSearch_Click);
        this.findViewById(R.id.ActMain_BtnMap).setOnClickListener(this.btnMap_Click);

        // The ListView to output the result
        this.lsvResult = (ListView) this.findViewById(R.id.ActMain_LsvResult);

        // The EditText for user's input to search
        this.edtSearch = (EditText) this.findViewById(R.id.ActMain_EdtSearch);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("searchText", this.edtSearch.getText().toString());
        savedInstanceState.putString("routesJSON", this.routesJSON);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null)
            return;

        try {
            // Restore the saved Routes and search Text
            String searchText = savedInstanceState.getString("searchText");
            if (searchText != null) {
                this.edtSearch.setText(searchText);
                this.edtSearch.setSelection(searchText.length());
            }
            String routesJSON = savedInstanceState.getString("routesJSON");
            if (routesJSON != null)
                this.updateRoutes(new JSONObject(routesJSON));
        } catch (Exception ex) {
            Toast.makeText(ActMain.this, "Error restoring the routes", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Search the WS for bus' destinations with the input text
     */
    private final View.OnClickListener btnSearch_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ActMain.this.edtSearch.getWindowToken(), 0);

                // Make the search
                ActMain.this.searchRoutes();
            } catch (Exception ex) {
                Toast.makeText(ActMain.this, "Error calling the Web Service", Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Open the Map dialog
     */
    private final View.OnClickListener btnMap_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Start the Map Activity expecting a result (street name)
            Intent mapIntent = new Intent(ActMain.this, ActMap.class);
            ActMain.this.startActivityForResult(mapIntent, ActMain.MAP_STREET_NAME);
        }
    };

    /**
     * Called when an Activity returns after been called for a result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // Identify the caller
            switch (requestCode) {
                case ActMain.MAP_STREET_NAME:
                    // Return if not OK
                    if (resultCode != RESULT_OK) {
                        Toast.makeText(ActMain.this, "Could not get the Street Name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Get the Street Name
                    String streetName = data.getStringExtra("streetName");

                    // Some validations
                    if (streetName == null) {
                        Toast.makeText(ActMain.this, "Invalid Street Name", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Make the search, setting in the GUI the street name
                    ActMain.this.edtSearch.setText(streetName);
                    ActMain.this.searchRoutes();
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(ActMain.this, "Error making the search", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Make the Call to the WS searching for the Routes
     */
    private void searchRoutes() {
        try {
            // Make the search at the WS
            WSParam wsParam = new WSParam(WSParam.URL_FIND_ROUTES_BY_STOPNAME, "stopName", "%" + ActMain.this.edtSearch.getText() + "%");
            WSTask wsTask = new WSTask(ActMain.this, ActMain.this.searchCallBack);
            wsTask.execute(wsParam);

//            // Mock the WS Call, using a pre-loaded JSON response
//            TaskResult result = new TaskResult();
//            result.setJsonObj(new JSONObject("{\"rows\":[{\"id\":17,\"shortName\":\"134\",\"longName\":\"BEIRA-MAR NORTE\",\"lastModifiedDate\":\"2013-03-04T03:00:00+0000\",\"agencyId\":9},{\"id\":22,\"shortName\":\"131\",\"longName\":\"AGRONÔMICA VIA GAMA D'EÇA\",\"lastModifiedDate\":\"2009-10-26T02:00:00+0000\",\"agencyId\":9},{\"id\":32,\"shortName\":\"133\",\"longName\":\"AGRONÔMICA VIA MAURO RAMOS\",\"lastModifiedDate\":\"2012-07-23T03:00:00+0000\",\"agencyId\":9},{\"id\":35,\"shortName\":\"185\",\"longName\":\"UFSC SEMI DIRETO\",\"lastModifiedDate\":\"2013-06-10T03:00:00+0000\",\"agencyId\":9}],\"rowsAffected\":0}"));
//            ActMain.this.searchCallBack.onComplete(result);
        } catch (Exception ex) {
            Toast.makeText(ActMain.this, "Error making the routes' search", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When the WS returns this method is called
     */
    private final TaskCallBack searchCallBack = new TaskCallBack() {
        @Override
        public void onComplete(TaskResult result) {
            try {
                switch (result.getStatus()) {
                    case TaskResult.STS_OK:
                        // Success calling the WS
                        ActMain.this.updateRoutes(result.getJsonObj());
                        break;

                    case TaskResult.STS_ERR:
                        // Error calling the WS
                        Toast.makeText(ActMain.this, result.getErrMsg(), Toast.LENGTH_LONG).show();
                        break;

                    case TaskResult.STS_UNKNOWN:
                    default:
                        // Status unknown
                        Toast.makeText(ActMain.this, "Status unknown!", Toast.LENGTH_LONG).show();
                        break;
                }
            } catch (Exception ex) {
                Toast.makeText(ActMain.this, "Problem unexpected", Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Update the Routes listView based on the JSON
     */
    private void updateRoutes(JSONObject routesJSON) {
        try {
            // Create the list
            final java.util.List<Route> listRoutes = new java.util.ArrayList<>();

            // To restore the data if the Activity is Restarted
            this.routesJSON = routesJSON.toString();

            JSONArray rows = routesJSON.getJSONArray("rows");
            for (int index = 0; index < rows.length(); index++) {
                JSONObject route = rows.getJSONObject(index);
                int id = route.getInt("id");
                String shortName = route.getString("shortName");
                String longName = route.getString("longName");
                listRoutes.add(new Route(id, shortName, longName));
            }

            // Set the list in the ListView
            ActMain.this.lsvResult.setAdapter(//
                    new ArrayAdapter<Route>(this, R.layout.listview_routes, listRoutes) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View row;
                            if (convertView == null) {
                                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                row = inflater.inflate(R.layout.listview_routes, parent);
                            } else {
                                row = convertView;
                            }
                            Route route = listRoutes.get(position);
                            ((TextView) row.findViewById(R.id.ListViewRoute_Text1)).setText(route.getShortName());
                            ((TextView) row.findViewById(R.id.ListViewRoute_Text2)).setText(route.getLongName());

                            return row;
                        }
                    });

            // Defines the item onclick event
            ActMain.this.lsvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Route route = listRoutes.get(position);

                    Intent actDetailsIntent = new android.content.Intent(ActMain.this, ActDetails.class);
                    actDetailsIntent.putExtra("routeId", route.getId());
                    actDetailsIntent.putExtra("routeName", route.getLongName());

                    ActMain.this.startActivity(actDetailsIntent);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(ActMain.this, "Problem updating the Route List", Toast.LENGTH_LONG).show();
        }
    }
}
