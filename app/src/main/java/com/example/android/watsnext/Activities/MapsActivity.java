package com.example.android.watsnext.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.watsnext.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {



    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private LocationManager mLocationManager;
    private TextView mAddressTextView;
    private Button mSaveAddressButton;
    private String mAddressString;

    public static final String EXTRA_ADDRESS = "extraAddress";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mAddressTextView = findViewById(R.id.tv_address);

        mSaveAddressButton = findViewById(R.id.button_save_location_address);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        } else {
            // If the location permission wasn't granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        mSaveAddressButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent resultIntent = new Intent(MapsActivity.this, AddEventActivity.class);
                resultIntent.putExtra(EXTRA_ADDRESS, mAddressString);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        // Add a marker to the current user position and move the camera
        if(mCurrentLocation != null) {
            LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Position"));
            CameraPosition target = CameraPosition.builder().target(currentLatLng).zoom(14).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        }
    }



    //Get address of location
    private String getAddressFromLocation(double latitude, double longitude){
        Address address;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0);
            String addressString = address.getAddressLine(0);
            return addressString;
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Helper method to get the user's current location
     */
    private Location getCurrentLocation() {
        Location currentLocation;

        try {
            currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return currentLocation;
        } catch (SecurityException e){
            Log.e(TAG, "Location permission denied");
        }
        return null;
    }

    /**
     * Initialize the Map
     */
    private void initializeMap(){
        mCurrentLocation = getCurrentLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeMap();
                }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Add a marker to the selected position
        MarkerOptions eventMarkerOptions = new MarkerOptions().position(latLng).title("Selected Position");

        // Markers have 2 roles. They show the user's current position and a confirmation role that the event location is correctly placed
        // Users can delete event location markers by clicking on them
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().equals("Selected Position")){
                    marker.remove();
                }
                return true;
            }
        });
        mMap.addMarker(eventMarkerOptions);
        CameraPosition target = CameraPosition.builder().target(latLng).zoom(14).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));

        mAddressString = getAddressFromLocation(latLng.latitude, latLng.longitude);
        mAddressTextView.setText(mAddressString);
    }
}
