package com.droidkit.pickers.map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droidkit.file.R;
import com.droidkit.pickers.map.util.Dimen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;

import java.util.HashSet;


public class MapPickerActivity extends ActionBarActivity
        implements
        GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMapLoadedCallback {

    private static final String LOG_TAG = "MapPickerActivity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location currentLocation;

    private Marker currentPick;

    private FloatingActionButton mDefineMyLocationButton;
    private FloatingActionButton mPickCurrentLocationButton;

    private TextView mTitle;
    private TextView mSubtitle;
    private LinearLayout mCurrentPickPanel;

    private boolean mIsAnimationProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dimen.init(this);
        setContentView(R.layout.picker_activity_map_picker);

        setUpMapIfNeeded();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {

            setSupportActionBar(toolbar);

            int paddingTop = Dimen.getStatusBarHeight();
            toolbar.setPadding(0, paddingTop, 0, 0);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        mDefineMyLocationButton = (FloatingActionButton) findViewById(R.id.define_my_location);
        mDefineMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Location location = mMap.getMyLocation();

                if (location != null) {

                    mMap.clear();
                    currentPick = null;

                    final LatLng target = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraPosition.Builder builder = new CameraPosition.Builder();
                    builder.zoom(17);
                    builder.target(target);

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

                    currentLocation = location;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            showMapCurrentPin(target);
                        }
                    }, 300);

                } else {

                    Toast.makeText(getBaseContext(), R.string.picker_map_pick_my_wait,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPickCurrentLocationButton = (FloatingActionButton) findViewById(R.id.select_location);
        mPickCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentLocation != null) {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("latitude", currentLocation.getLatitude());
                    returnIntent.putExtra("longitude", currentLocation.getLongitude());

                    setResult(RESULT_OK, returnIntent);
                    finish();

                } else {

                    final LocationManager manager =
                            (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                        Toast.makeText(MapPickerActivity.this, R.string.picker_map_pick_my_wait,
                                Toast.LENGTH_SHORT).show();

                    } else {

                        buildAlertMessageNoGps();
                    }
                }
            }
        });

        final LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER )
                || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {

            // okay navigation is enabled

        } else {

            buildAlertMessageNoGps();
        }

        mCurrentPickPanel = (LinearLayout) findViewById(R.id.current_pick);
        mTitle = (TextView) findViewById(R.id.current_pick_title);
        mSubtitle = (TextView) findViewById(R.id.current_pick_subtitle);

    }

    private void togglePicker(final boolean show) {

        if (mIsAnimationProcessing) return;

        mIsAnimationProcessing = true;

        final ObjectAnimator togglePanel = ObjectAnimator.ofFloat(
                mCurrentPickPanel,
                "translationY",
                show ? 0 : Dimen.getPX(R.dimen.picker_map_translate_panel)
        );

        final ObjectAnimator toggleDefine = ObjectAnimator.ofFloat(
                mDefineMyLocationButton,
                "translationY",
                show ? Dimen.getPX(R.dimen.picker_map_translate_nav_button) : 0
        );

        final ObjectAnimator togglePickX =
                ObjectAnimator.ofFloat(mPickCurrentLocationButton, "scaleX", show ? 1 : 0);

        final ObjectAnimator togglePickY =
                ObjectAnimator.ofFloat(mPickCurrentLocationButton, "scaleY", show ? 1 : 0);

        final HashSet<Animator> toggleCurrent = new HashSet<Animator>() {{

            add(togglePanel);
            add(toggleDefine);
        }};

        final HashSet<Animator> toggleSelect = new HashSet<Animator>() {{

            add(togglePickX);
            add(togglePickY);
        }};

        AnimatorSet startAnimator = new AnimatorSet();
        startAnimator.setInterpolator(new DecelerateInterpolator());
        startAnimator.setDuration(200);

        startAnimator.playTogether(show ? toggleCurrent : toggleSelect);

        startAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Log.d("ON FIRST END BOOLEAN", "VALUE: " + show);

                if (show)
                    mPickCurrentLocationButton.setVisibility(View.VISIBLE);
                else
                    mPickCurrentLocationButton.setVisibility(View.GONE);

                AnimatorSet endAnimator = new AnimatorSet();
                endAnimator.setInterpolator(new DecelerateInterpolator());
                endAnimator.setDuration(200);

                endAnimator.playTogether(show ? toggleSelect : toggleCurrent);

                endAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        Log.d("ON SECOND END BOOLEAN", "VALUE: " + show);

                        if (!show)
                            mCurrentPickPanel.setVisibility(View.GONE);

                        mIsAnimationProcessing = false;
                    }
                });

                endAnimator.start();
            }
        });

        startAnimator.start();
    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                       @SuppressWarnings("unused") final int id) {

                       startActivity(new Intent(
                               android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
                       ));
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {

                   public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                       dialog.cancel();
                   }
               });

        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                setUpMap();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = null;

        for (String provider : locationManager.getAllProviders()) {

            lastKnownLocation = locationManager.getLastKnownLocation(provider);

            if (lastKnownLocation != null) break;
        }

        if (lastKnownLocation != null) {

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(

                    new LatLng(
                            lastKnownLocation.getLatitude(),
                            lastKnownLocation.getLongitude()),
                            14
                    )
            );
        }

        mMap.setOnMyLocationChangeListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapLoadedCallback(this);
    }

    void hideKeyBoard() {

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        );

        View focusedView = this.getCurrentFocus();

        if (focusedView != null) {

            InputMethodManager inputMethodManager =
                    (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(

                    focusedView.getWindowToken(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY
            );
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

        if (currentLocation == null) {

            // do we need to attach our location on the start?
            this.currentLocation = location;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), 14)
            );

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    showMapCurrentPin(
                            new LatLng(
                                    currentLocation.getLatitude(),
                                    currentLocation.getLongitude()
                            )
                    );
                }
            }, 300);
        }

        this.currentLocation = location;

        Log.d("Location changed", location.toString());
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if (currentPick == null) {

            mMap.clear();

            MarkerOptions currentPickOptions = new MarkerOptions()
                    .draggable(true)
                    .position(latLng);

            currentPick = mMap.addMarker(currentPickOptions);

        } else {

            currentPick.setPosition(latLng);
        }

        showMapCurrentPin(latLng);
    }



    public void showMapCurrentPin(LatLng position) {

        mCurrentPickPanel.setVisibility(View.VISIBLE);
        mTitle.setText(R.string.picker_loading);
        mSubtitle.setText("");

        togglePicker(true);

        AddressTask task = new AddressTask(this, position) {
            @Override
            protected void onPostExecute(Address s) {

                if (s != null) {

                    if (s.getMaxAddressLineIndex() > 0) {

                        mTitle.setText(s.getAddressLine(0));
                    }

                    if (s.getLocality() != null) {

                        mSubtitle.setText(s.getLocality() + ", " + s.getCountryName());

                    } else {

                        if (s.getCountryName() != null) {

                            mSubtitle.setText(s.getCountryName());
                        }
                    }

                } else {

                    mTitle.setText(R.string.picker_empty);
                    mSubtitle.setText("");
                }
            }
        };
        task.execute();

    }

    private void hideMapCurrentPin() {

        togglePicker(false);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if (mCurrentPickPanel.isShown()) {

            mMap.clear();
            currentPick = null;

            hideMapCurrentPin();

        } else {

            if (currentPick == null) {

                mMap.clear();

                MarkerOptions currentPickOptions = new MarkerOptions()
                        .draggable(true)
                        .position(latLng);

                currentPick = mMap.addMarker(currentPickOptions);

            }

            showMapCurrentPin(latLng);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        // TODO stub

    }

    @Override
    public void onMapLoaded() {

        Toast.makeText(this, "Tap on the map to pick location", Toast.LENGTH_SHORT).show();
    }
}