package com.example.fwipapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
        LocationListener{

    // Private variables, used in the functions defined below
    private GoogleMap mMap;

    private int numMarkers;

    private Marker mAnnArbor;

    private static final LatLng ANNARBOR = new LatLng(42.2808, -83.7430);

    public final static String EXTRA_MESSAGE = "some message?";

    @Override
    public void onLocationChanged(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(loc));
        if(mMap != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        }
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(this, "FUCK YOU AND YOUR PERMISSIONS", Toast.LENGTH_SHORT);
            }
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
        mMap = googleMap;

        // Add a marker in Ann Arbor and move the camera
        mAnnArbor = mMap.addMarker(new MarkerOptions()
                .position(ANNARBOR)
                .title("Marker in Ann Arbor")
                .snippet("Fucc yea"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ANNARBOR));
        //can use setTag and getTag to set/get data with
        /*
        Your app may cater for different types of markers, and you want to treat them
        differently when the user clicks them. To accomplish this, you can store a String
        with the marker indicating the type.

        OR we could create some sort of class or something that stores all the data.
         */
        mAnnArbor.setTag(0);
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

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onMapClick(LatLng point)
    {
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("New marker number " + Integer.toString(numMarkers))
                .snippet("muahahha"));
        return;
    }
    public void makeNewEvent(View view)
    {
        Intent intent = new Intent(this, Post_Event.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "Some message!";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
