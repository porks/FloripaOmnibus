package org.porks.arctouch.floripaomnibus;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class ActMap extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener {
    /**
     * Fixed location at the 'Hercílio Luz' bridge.<br />
     * We will use this as the start location in Florianópolis
     */
    private static final double latitude_HLuz = -27.593817;
    private static final double longitude_HLuz = -48.565482;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actmap);

        // Get the MapFragment and inform the async callback listener
        // to be called when the map is ready
        MapFragment gMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.ActMap_Map);
        gMapFragment.getMapAsync(this);
    }

    /**
     * Callback to be triggered when the map is ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Map is ready to be used.
            GoogleMap gMap = googleMap;

            // Set the Long Click listener to set the position where the users want to search for bus routes
            gMap.setOnMapLongClickListener(this);

            // We will fix our start location at the Hercílio Luz Bridge,
            // just for sake of this exercise (it is about Florianóplis)
            final LatLng fixedStart = new LatLng(ActMap.latitude_HLuz, ActMap.longitude_HLuz);

            // Map a Sample Marker int the map
            MarkerOptions marker = new MarkerOptions().title("Start Point").position(fixedStart)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            gMap.addMarker(marker);

            // Position the map in our current location
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(fixedStart, 15);
            gMap.moveCamera(update);
        } catch (Exception ex) {
            Toast.makeText(ActMap.this, "Error starting the Google Maps", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        try {
            // Get the Location information from Google Maps API
            Geocoder gc = new Geocoder(ActMap.this, Locale.getDefault());
            List<Address> addresses = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);

            // We have no information
            if (addresses.size() == 0)
                return;

            // Get the street name
            String streetName = addresses.get(0).getThoroughfare();

            // Return for the calling Activity informing the street name
            Intent resultIntent = new Intent();
            resultIntent.putExtra("streetName", streetName);
            ActMap.this.setResult(RESULT_OK, resultIntent);
            ActMap.this.finish();
        } catch (Exception ex) {
            Toast.makeText(ActMap.this, "Error getting the street address", Toast.LENGTH_LONG).show();
        }
    }
}
