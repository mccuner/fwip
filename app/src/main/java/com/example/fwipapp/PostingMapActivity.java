package com.example.fwipapp;

// Libraries
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.Manifest;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import com.parse.*;

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
        LocationListener,
        OnMarkerDragListener,
        OnInfoWindowClickListener {

    /*
     * Global constants
     */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PostingMap Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /*
     * Class for event data
     */
    private class EventData {
        private String Name;
        private String Description;
        private String Food;
        private Date Created;
        private Date Last_Updated;
        private Calendar Clear_Time;

        // constructor
        public EventData() {
            this.Clear_Time = Calendar.getInstance();
            this.Clear_Time.setTime(new Date());
            this.Created = Clear_Time.getTime(); //now
            this.Clear_Time.add(Calendar.HOUR_OF_DAY, 1); //adds 1 hour
        }

        public EventData(String name, String description, String food) {
            this.Name = name;
            this.Description = description;
            this.Food = food;
            this.Clear_Time = Calendar.getInstance();
            this.Clear_Time.setTime(new Date());
            this.Created = Clear_Time.getTime(); //now
            this.Clear_Time.add(Calendar.HOUR_OF_DAY, 1); //adds 1 hour
        }

        //getters
        public String getName() {
            return Name;
        }

        public String getDesc() {
            return Description;
        }

        public String getFood() {
            return Food;
        }

        public Date getCreated() {
            return Created;
        }

        public Date getLast_Updated() {
            return Last_Updated;
        }

        public Date getClear_Time() {
            return Clear_Time.getTime();
        }

        //setters
        public void setName(String new_name) {
            this.Name = new_name;
        }

        public void setDesc(String new_desc) {
            this.Description = new_desc;
        }

        public void setFood(String new_food) {
            this.Food = new_food;
        }

        public void setDates(Date time) {
            this.Created = this.Last_Updated = time;
            this.Clear_Time.setTime(time);
            addTime();
        }

        public void setLast_Updated(Date time) {
            this.Last_Updated = time;
        }

        public void addTime() {
            // will change with time - lol puns
            this.Clear_Time.add(Calendar.HOUR_OF_DAY, 1);
        }

        public void subTime() {
            this.Clear_Time.add(Calendar.HOUR_OF_DAY, -1);
        }
    }

    /*
     * Global variables
     */
    private GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    Location foodLocation;  // This is the last known location of the food marker
    Marker foodMarker;      // This is the purple marker
    LocationRequest mLocationRequest;
    SupportMapFragment mapFrag;
    private List<Marker> markerList;
    private String new_name;
    private String new_desc;
    private String new_food;
    private EventData new_event_data = new EventData();
    private HashMap allMarkersMap = new HashMap<Marker, EventData>();
    private PopupWindow new_event_window;
    private LayoutInflater new_event_inflater;
    private FrameLayout new_event_layout;

    private Button help_button;
    private PopupWindow help_window;
    private LayoutInflater help_inflater;
    private FrameLayout help_layout;

    // TODO: might not need this or idk
    @Override
    public void onInfoWindowClick(Marker marker) {}

    /*
     * Custom Info Window Class
     */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Log.d("M", "GET INFO CONTENTS");
            render(marker, mWindow);
            return mWindow;
        }

        private void render(Marker marker, View view) {
            Log.d("M", "RENDER");

            // Set the event title
            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleUi.setText(titleText);

            } else {
                titleUi.setText("");
            }

            // Parse the snippet for the description and food types
            String snippet = marker.getSnippet();
            String[] parts = snippet.split("---");
            TextView descUi = ((TextView) view.findViewById(R.id.description));
            if (parts[0] != null) {
                SpannableString snippetText = new SpannableString(parts[0]);
                descUi.setText(snippetText);
            } else {
                descUi.setText("");
            }
            TextView foodUi = ((TextView) view.findViewById(R.id.foodtypes));
            if (parts.length > 1) {
                if (parts[1] != null) {
                    SpannableString snippetText = new SpannableString(parts[1]);
                    foodUi.setText(snippetText);
                } else {
                    foodUi.setText("");
                }
            }
            TextView dateUi = ((TextView) view.findViewById(R.id.dateTime));
            if (parts.length > 2) {
                SpannableString dateText = new SpannableString(parts[2]);
                dateUi.setText(dateText);
            } else {
                dateUi.setText("");
            }
        }
    }

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

    /*
     * Runs on when Fwip is started
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("M", "ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_map);

        // connect to parse database
        Parse.initialize(new Parse.Configuration.Builder(this.getApplicationContext())
                .applicationId(getString(R.string.parse_app_id))
                .clientKey(getString(R.string.parse_client_id))
                .server("https://parseapi.back4app.com")
                .build()
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        if (markerList == null) {
            markerList = new ArrayList<>();
        }

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = "nothing, there is no intent/no extras";
            } else {
                newString = "data arrived with intent";
                new_name = extras.getString("name");
                new_desc = extras.getString("desc");
                new_food = extras.getString("food");

            }
        } else {
            newString = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }
        Log.d("M", newString);

        final Button new_event_button = (Button) findViewById(R.id.new_event_button);
        new_event_layout = (FrameLayout) findViewById(R.id.map);
        new_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_event_button.setVisibility(View.GONE);
                final Button finalize_button = (Button) findViewById(R.id.save_event_button);
                new_event_inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final ViewGroup container = (ViewGroup) new_event_inflater.inflate(R.layout.content_post__event, null);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int width = dm.widthPixels;
                int height = dm.heightPixels;

                // Event window for creating event
                new_event_window = new PopupWindow(container, (int) (width * 0.96), (int) (height * 0.95), false);
                new_event_window.setTouchable(true);
                new_event_window.setOutsideTouchable(false);
                new_event_window.setFocusable(true);
                new_event_window.update();
                new_event_window.showAtLocation(new_event_layout, Gravity.NO_GRAVITY, 33, 100);

                // make new, draggable marker
                if (checkLocationPermission()) {
                    Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    foodMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("New Food Marker")
                            .snippet("Drag Me!")
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                }

                Button cancel_button = (Button) container.findViewById(R.id.Cancel);
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        foodMarker.remove();
                        new_event_window.dismiss();
                        new_event_button.setVisibility(View.VISIBLE);
                    }
                });
                Button save_button = (Button) container.findViewById(R.id.SaveData);
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get data from forms
                        EditText name_el = (EditText) container.findViewById(R.id.InputName);
                        name_el.setTextColor(Color.BLACK);
                        new_name = name_el.getText().toString();
                        EditText desc_el = (EditText) container.findViewById(R.id.InputDescription);
                        desc_el.setTextColor(Color.BLACK);
                        new_desc = desc_el.getText().toString();
                        EditText food_el = (EditText) container.findViewById(R.id.InputFood);
                        food_el.setTextColor(Color.BLACK);
                        new_food = food_el.getText().toString();

                        new_event_data.setName(new_name);
                        new_event_data.setDesc(new_desc);
                        new_event_data.setFood(new_food);

                        // sets created time to now, last_upt to now, clear_time to an hour from now
                        new_event_data.setDates(new Date());

                        new_event_window.dismiss();
                        final Button cancel_event_button = (Button) findViewById(R.id.cancel_event_button);
                        finalize_button.setVisibility(View.VISIBLE);
                        cancel_event_button.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        // Create 2 new buttons at bottom of screen
        final Button finalize_button = (Button) findViewById(R.id.save_event_button);
        final Button cancel_event_button = (Button) findViewById(R.id.cancel_event_button);

        // Listener for submission button
        finalize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng location = foodMarker.getPosition();
                Marker new_event_marker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(new_name)
                        .snippet(new_desc + "---" + new_food + "---" + new_event_data.getCreated()));

                // save location and data with marker
                allMarkersMap.put(new_event_marker, new_event_data);
//                markerList.add(new_event_marker);
                ParseObject new_parse_marker = new ParseObject("Events");
                new_parse_marker.put("latitude", new_event_marker.getPosition().latitude);
                new_parse_marker.put("longitude", new_event_marker.getPosition().longitude);
                new_parse_marker.put("name", new_name);
                new_parse_marker.put("snippet", new_event_marker.getSnippet());
                new_parse_marker.put("clear_time", new_event_data.getClear_Time());
                new_parse_marker.saveInBackground();

                // remove purple marker
                foodMarker.remove();
                finalize_button.setVisibility(View.GONE);
                new_event_button.setVisibility(View.VISIBLE);
                cancel_event_button.setVisibility(View.GONE);
            }
        });


        cancel_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove purple marker
                foodMarker.remove();

                // reset the bottom button to "New Event"
                finalize_button.setVisibility(View.GONE);
                cancel_event_button.setVisibility(View.GONE);
                new_event_button.setVisibility(View.VISIBLE);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*
     * Runs when Fwip is moved to the background
     */
    @Override
    public void onPause() {
        Log.d("M", "ON PAUSE");
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /*
     * Runs when Fwip is connected
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("M", "ON CONNECTED");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(200);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("M", "REQUESTING LOCATION");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest,
                    this);
        }
    }

    /*
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                Log.d("M", "SET LOCATION TRUE");
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            Log.d("M", "SET LOCATION TRUE");
            mMap.setMyLocationEnabled(true);
        }

        // Get rid of toolbar
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Set location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Set all of the listeners
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(this);

        // Set the new Info Window
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        loadMarkersFromDatabase();
    }

    /*
     * Called when the user clicks a marker
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.d("M", "ON MARKER CLICK");
        marker.showInfoWindow();
        return false;
    }

    /*
     * Called when the user's location changes
     */
    public void onLocationChanged(Location location) {
        Log.d("M", "ON LOCATION CHANGED");
        Log.d("LAT", String.valueOf(location.getLatitude()));
        Log.d("LNG", String.valueOf(location.getLongitude()));
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    /*
     * Used as an onclick handler for the help button
     * Method must be public and accept a view as a parameter
     */
    public void onHelpClick(View view) {
        help_button = (Button) findViewById(R.id.help_button);
        help_layout = (FrameLayout) findViewById(R.id.map);

        help_inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup help_container = (ViewGroup) help_inflater.inflate(R.layout.help_menu, null);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        help_window = new PopupWindow(help_container, (int) (width * 0.95), (int) (height * 0.97), true);
        help_window.setOutsideTouchable(false);
        help_window.showAtLocation(help_layout, Gravity.NO_GRAVITY, 33, 60);
        help_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help_window.dismiss();
            }
        });
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
            } else {
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
                } else {
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
     * Function for loading markers from the Parse database
     */
    private void loadMarkersFromDatabase() {
        Log.d("M", "loading markers from parse...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // successful
                    for (ParseObject this_marker : objects) {
                        LatLng new_position = new LatLng(this_marker.getDouble("latitude"), this_marker.getDouble("longitude"));
                        mMap.addMarker(new MarkerOptions().position(new_position)
                                .title(this_marker.getString("name"))
                                .snippet(this_marker.getString("snippet")));
                    }
                } else {
                    // failed
                    Log.d("M", "Marker retrieval from parse failed.");
                }
            }
        });
    }

    @Override
    public void onMapClick(LatLng point) {}

    @Override
    public void onMarkerDragStart(Marker marker) {}

    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}


