package com.example.fwipapp;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

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
        OnMapClickListener {

    // Private variables, used in the functions defined below
    private GoogleMap mMap;

    private int numMarkers;

    private Marker mAnnArbor;

    private static final LatLng ANNARBOR = new LatLng(42.2808, -83.7430);

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
        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("New marker number " + Integer.toString(numMarkers))
                .snippet("muahahha"));
        return;
    }

}
