package com.example.fwipapp;

// Libraries
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.Manifest;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.location.Location;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    /*
     * Private variables, used in the functions defined below
     */
    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private int numMarkers;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFrag;
    SharedPreferences mSharedPrefs = null;
    private List<Marker> markerList;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String new_name;
    private String new_desc;
    private String new_food;

    /*
     * Function used to build the Google Play API client
     */
    protected synchronized void buildGoogleApiClient() {
        Log.d("M", "BUILD GOOGLE API CLIENT");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private PopupWindow new_event_window;
    private LayoutInflater new_event_inflater;
    private FrameLayout new_event_layout;

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

    /*
     * Runs on when Fwip is started
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("M", "ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_map);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        if(markerList == null) {
            markerList = new ArrayList<>();
        }

        String newString = "some random string";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= "nothing, there is no intent/no extras";
            } else {
                newString = "data arrived with intent";
                new_name = extras.getString("name");
                new_desc = extras.getString("desc");
                new_food = extras.getString("food");

            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }
        Log.d("M", newString);

        Button new_event = (Button) findViewById(R.id.new_event_button);
        new_event_layout = (FrameLayout) findViewById(R.id.map);
        new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new_event_inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) new_event_inflater.inflate(R.layout.content_post__event,null);
                new_event_window = new PopupWindow(container,1370,925,false);
                new_event_window.showAtLocation(new_event_layout, Gravity.NO_GRAVITY,38,1450);
                new_event_window.setFocusable(true);
                new_event_window.update();

                Button cancel_button = (Button) container.findViewById(R.id.Cancel);
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new_event_window.dismiss();
                    }
                });
                Button save_button = (Button) container.findViewById(R.id.Save);
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get data from forms
                        // place a marker
                        // save marker
                        // do some more mumbo jumbo
                        new_event_window.dismiss();
                    }
                });
            }
        });
    }

    /*
     * Runs when Fwip is moved to the background
     */
    @Override
    public void onPause() {
        Log.d("M", "ON PAUSE");
        savePreferences();
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
        Log.d("M", "ON CONNECTED");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("M", "REQUESTING LOCATION");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest,
                    this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mLastLocation != null) {
            Log.d("M", "INITIALIZING LOCATION");
            onLocationChanged(mLastLocation);
        }
    }

    /**
     * Manipulates the map once available
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("M", "ON MAP READY");
        mMap = googleMap;

        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                Log.d("M", "SET LOCATION TRUE");
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            Log.d("M", "SET LOCATION TRUE");
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        loadPreferences();
    }

    /*
     * Called when the user clicks a marker
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.d("M", "ON MARKER CLICK");
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount++;
            marker.setTag(clickCount);
            Toast.makeText(this, marker.getTitle() + " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /*
     * Called when the user clicks the map
     */
    @Override
    public void onMapClick(LatLng point) {
        Log.d("M", "ON MAP CLICK");
        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("New marker number " + Integer.toString(numMarkers++))
                .snippet("muahahha"));
        newMarker.setTag(0);
        newMarker.setTitle("stab marker");
        markerList.add(newMarker);
        savePreferences();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    /*
     * Called when the user's location changes
     */
    public void onLocationChanged(Location location) {
        Log.d("M", "ON LOCATION CHANGED");
        mLastLocation = location;
        Log.d("LAT", String.valueOf(mLastLocation.getLatitude()));
        Log.d("LONG", String.valueOf(mLastLocation.getLongitude()));
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        // Move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        // Stop updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /*
     * Checks the user's location permissions
     */
    public boolean checkLocationPermission() {
        Log.d("M", "CHECK LOCATION PERMISSION");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            Log.d("M", "PERMISSION GRANTED");
            return true;
        }
    }
    public void makeNewEvent(View view) {
        Intent intent = new Intent(this, Post_Event.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "Some message!";

        startActivity(intent);
    }

    public void clearMarkers(View view) {
        Toast.makeText(this, "Clearing Markers", Toast.LENGTH_SHORT).show();
        SharedPreferences mSharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int size = mSharedPreferences.getInt("listSize", 0);
        for(int i = 0; i < size; ++i) {
            Marker victim = markerList.get(i);
            victim.remove();
        }
        markerList.clear();
        editor.clear();
        editor.apply();
    }

    /*
     * Enables location if allowed by the user's permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d("M", "ON REQUEST PERMISSION RESULT");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                }
                else {
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
     * Saves all map markers
     */
    private void savePreferences() {
        SharedPreferences mSharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("listSize", markerList.size());
        for (int i = 0; i < markerList.size(); ++i) {
            editor.putFloat("Lat" + i, (float) markerList.get(i).getPosition().latitude);
            editor.putFloat("Lng" + i, (float) markerList.get(i).getPosition().longitude);
            editor.putString("Title" + i, markerList.get(i).getTitle());
        }
        editor.apply();
    }

    /*
     * Loads all map markers
     */
    private void loadPreferences() {
        SharedPreferences mSharedPreferences = getPreferences(MODE_PRIVATE);
        int size = mSharedPreferences.getInt("listSize", 0);
        for(int i = 0; i < size; ++i) {
            double lat = (double) mSharedPreferences.getFloat("Lat" + i, 0);
            double lng = (double) mSharedPreferences.getFloat("Lng" + i, 0);
            String title = mSharedPreferences.getString("Title" + i, "NULL");
            markerList.add(mMap.addMarker(new MarkerOptions().position(
                    new LatLng(lat, lng)).title(title)));
        }
    }
}
