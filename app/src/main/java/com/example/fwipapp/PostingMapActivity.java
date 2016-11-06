package com.example.fwipapp;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
// Android Libraries
import android.util.Log;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import android.location.Location;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

// Google Libraries
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;

//copy/pasted imports
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

/*
    TODO: Be able to view info about markers
    TODO: Be able to place markers
    TODO: Be able to edit information for markers
    TODO: Have custom information for markers
    TODO: Perhaps add buttons? If time? (eventually)
    TODO: customize marker image based on type of event (eventually)
 */

public class PostingMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        OnMarkerClickListener,
        OnMapClickListener,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    // Private variables, used in the functions defined below
    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    public String lastLat, lastLong;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private int numMarkers;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFrag;

    /*
     * Function used to build the Google Play API client
     */
    protected synchronized void buildGoogleApiClient() {
        Log.d("M", "BUILDGOOGLEAPICLIENT");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public final static String EXTRA_MESSAGE = "some message?";

    public enum perm_reqs {MY_PERMISSIONS_REQUEST_FINE_LOCATION, SOMETHING}

    /* This is the custom info window. which I don't wanna see yet. */

    // this is how we will customize the info window. we are gonna need to change this up,
    // and make sure that we include the correct classes and whatnot
//    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
//
//        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
//        // "title" and "snippet".
//        private final View mWindow;
//
//        private final View mContents;
//
//        CustomInfoWindowAdapter() {
//            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
//            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
//                // This means that getInfoContents will be called.
//                return null;
//            }
//            render(marker, mWindow);
//            return mWindow;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//                // This means that the default info contents will be used.
//                return null;
//            }
//            render(marker, mContents);
//            return mContents;
//        }
//
//        private void render(Marker marker, View view) {
//            int badge;
//            // Use the equals() method on a Marker to check for equals.  Do not use ==.
//            if (marker.equals(mBrisbane)) {
//                badge = R.drawable.badge_qld;
//            } else if (marker.equals(mAdelaide)) {
//                badge = R.drawable.badge_sa;
//            } else if (marker.equals(mSydney)) {
//                badge = R.drawable.badge_nsw;
//            } else if (marker.equals(mMelbourne)) {
//                badge = R.drawable.badge_victoria;
//            } else if (marker.equals(mPerth)) {
//                badge = R.drawable.badge_wa;
//            } else {
//                // Passing 0 to setImageResource will clear the image view.
//                badge = 0;
//            }
//            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);
//
//            String title = marker.getTitle();
//            TextView titleUi = ((TextView) view.findViewById(R.id.title));
//            if (title != null) {
//                // Spannable string allows us to edit the formatting of the text.
//                SpannableString titleText = new SpannableString(title);
//                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
//                titleUi.setText(titleText);
//            } else {
//                titleUi.setText("");
//            }
//
//            String snippet = marker.getSnippet();
//            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
//            if (snippet != null && snippet.length() > 12) {
//                SpannableString snippetText = new SpannableString(snippet);
//                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
//                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
//                snippetUi.setText(snippetText);
//            } else {
//                snippetUi.setText("");
//            }
//        }
//    }

//    protected void createLocationRequest() {
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
//                        builder.build());
//    }

    /*
     * Runs on when Fwip is started
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("M", "ONCREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_map);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    /*
     * Runs when Fwip is moved to the background
     */
    @Override
    public void onPause() {
        Log.d("M", "ONPAUSE");
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /*
     * Runs on when Fwip is connected
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("M", "ONCONNECTED");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("M", "REQUESTINGLOCATION");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mLastLocation != null) {
            Log.d("M", "INITIALIZINGLOCATION");
            onLocationChanged(mLastLocation);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Ann Arbor.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("M", "ONMAPREADY");
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                Log.d("M", "SETLOCATIONTRUE");
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            Log.d("M", "SETLOCATIONTRUE");
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Location things
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "FUCK YOU AND YOUR LOCATION", Toast.LENGTH_SHORT);
        }
        else mMap.setMyLocationEnabled(true);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    /* Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.d("M", "ONMARKERCLICK");
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount++;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() + " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng point) {
        Log.d("M", "ONMAPCLICK");
        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("New marker number " + Integer.toString(numMarkers++))
                .snippet("muahahha"));
        newMarker.setTag(0);
        newMarker.setTitle("stab marker");
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    public void onLocationChanged(Location location) {
        Log.d("M", "ONLOCATIONCHANGED");
        mLastLocation = location;
        Log.d("LAT", String.valueOf(mLastLocation.getLatitude()));
        Log.d("LONG", String.valueOf(mLastLocation.getLongitude()));
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission() {
        Log.d("M", "CHECKLOCATIONPERMISSION");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //TODO:
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                //(just doing it here for now, note that with this code, no explanation is shown)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            Log.d("M", "PERMISSIONGRANTED");
            return true;
        }
    }
    public void makeNewEvent(View view) {
        Intent intent = new Intent(this, Post_Event.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "Some message!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d("M", "ONREQUESTPERMISSIONRESULT");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
