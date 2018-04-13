package com.example.android.watsnext.activities;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, LoaderManager.LoaderCallbacks<String> {


    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private LocationManager mLocationManager;
    private TextView mAddressTextView;
    private Button mSaveAddressButton;
    private FloatingActionButton mWeatherButton;
    private String mAddressString;
    private boolean isConnected;
    private String mWeatherDescription;
    private View mRootView;
    private static final String BASE_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String QUERY_PARAM_LATITUDE = "lat";
    private static final String QUERY_PARAM_LONGITUDE = "lon";
    private static final String QUERY_PARAM_API_KEY = "appid";

    //TODO: insert api key here
    private static final String API_KEY_VALUE = "API_KEY";


    public static final String EXTRA_ADDRESS = "extraAddress";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private static final int WEATHER_LOADER_ID = 747;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mRootView = findViewById(R.id.maps_root_layout);

        // Check network connectivity;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mAddressTextView = findViewById(R.id.tv_address);

        mSaveAddressButton = findViewById(R.id.button_save_location_address);
        mWeatherButton = findViewById(R.id.fab_weather_info);

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

        mSaveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(MapsActivity.this, AddEventActivity.class);
                resultIntent.putExtra(EXTRA_ADDRESS, mAddressString);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        mWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected) {
                    getLoaderManager().initLoader(WEATHER_LOADER_ID, null, MapsActivity.this);
                } else {
                    Snackbar.make(mRootView, R.string.no_internet_message, Snackbar.LENGTH_LONG).show();
                }
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

        if (mCurrentLocation != null) {
            LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Position"));
            CameraPosition target = CameraPosition.builder().target(currentLatLng).zoom(14).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        }
    }


    //Get address of location
    private String getAddressFromLocation(double latitude, double longitude) {
        Address address;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0);
            String addressString = address.getAddressLine(0);
            return addressString;
        } catch (Exception e) {
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
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission denied");

        }
        return null;
    }

    /**
     * Initialize the Map
     */
    private void initializeMap() {
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
                if (marker.getTitle().equals("Selected Position")) {
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


    /**
     * Helper method that builds the weather url based on current latitude and longitude
     */
    private URL buildWeatherUrl() {
        String latitude = String.valueOf(mCurrentLocation.getLatitude());
        String longitude = String.valueOf(mCurrentLocation.getLongitude());

        Uri.Builder uriBuilder = Uri.parse(BASE_WEATHER_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM_LATITUDE, latitude)
                .appendQueryParameter(QUERY_PARAM_LONGITUDE, longitude)
                .appendQueryParameter(QUERY_PARAM_API_KEY, API_KEY_VALUE);

        URL weatherURL = null;
        try {
            weatherURL = new URL(uriBuilder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return weatherURL;
    }

    /**
     * Make the http request
     *
     * @return - returns the JSON string
     * @throws IOException
     */
    private String makeWeatherHttpRequest(URL url) throws IOException {

        if (url == null || TextUtils.isEmpty(url.toString())) return null;

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            StringBuilder stringBuilder = new StringBuilder();

            while (scanner.hasNext()) {
                stringBuilder.append(scanner.next());
            }

            return stringBuilder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    private void parseWeatherJSON(String jsonResponse) {

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray weatherArray = baseJsonObject.getJSONArray("weather");
            JSONObject weatherJsonObject = weatherArray.getJSONObject(0);
            mWeatherDescription = weatherJsonObject.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    String jsonResponse = makeWeatherHttpRequest(buildWeatherUrl());
                    return jsonResponse;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        parseWeatherJSON(data);
        Snackbar.make(mRootView, mWeatherDescription, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
