package com.droidkit.pickers.map;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.droidkit.file.R;
import com.droidkit.pickers.map.util.Dimen;
import com.droidkit.pickers.map.util.OrientationHelper;
import com.droidkit.pickers.view.SearchViewHacker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapPickerActivity extends Activity
        implements
        AdapterView.OnItemClickListener,
        GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        AbsListView.OnScrollListener, GoogleMap.OnMapClickListener, GoogleMap.OnCameraChangeListener {

    private static final String LOG_TAG = "MapPickerActivity";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location currentLocation;

    private PlaceFetchingTask fetchingTask;
    private Marker currentPick;

    View select;
    private ListView list;
    private TextView status;
    private View header;
    private ProgressBar loading;
    private SearchView searchView;
    private ImageView fullSizeButton;
    private View listHolder;
    private View mapHolder;
    private View defineMyLocationButton;
    private TextView accuracyView;
    private View pickCurrentLocationButton;

    private HashMap<String, Marker> markers;
    private ArrayList<MapItem> places;
    private View controllers;
    private TextView title;
    private TextView subtitle;
    private Address currentPickedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dimen.initialize(this);
        setContentView(R.layout.picker_activity_map_picker);
        list = (ListView) findViewById(R.id.list);
        list.setOnScrollListener(this);
        list.setOnItemClickListener(this);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        loading = (ProgressBar) findViewById(R.id.loading);
        status = (TextView) findViewById(R.id.status);
        header = findViewById(R.id.header);
        listHolder = findViewById(R.id.listNearbyHolder);
        mapHolder = findViewById(R.id.mapholder);
        accuracyView = (TextView) findViewById(R.id.accurance);
        mapHolder.post(new Runnable() {
            @Override
            public void run() {
                if(OrientationHelper.getScreenOrientation(getWindowManager())==OrientationHelper.LANDSCAPE){
                    defaultMapHolderSize = mapHolder.getWidth();
                }else {
                    defaultMapHolderSize = mapHolder.getHeight();
                }
            }
        });
        setUpMapIfNeeded();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        fullSizeButton = (ImageView) findViewById(R.id.full);
        fullSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlacesList();
            }
        });

        defineMyLocationButton = findViewById(R.id.define_my_location);
        defineMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = mMap.getMyLocation();

                if (location != null) {

                    LatLng target = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraPosition.Builder builder = new CameraPosition.Builder();
                    builder.zoom(17);
                    builder.target(target);

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

                }else{
                    Toast.makeText(getBaseContext(), R.string.picker_map_pick_my_wait, Toast.LENGTH_SHORT).show();

                }
            }
        });

        pickCurrentLocationButton = findViewById(R.id.pick_current);
        pickCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocation != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("latitude", currentLocation.getLatitude());
                    returnIntent.putExtra("longitude", currentLocation.getLongitude());

                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        Toast.makeText(MapPickerActivity.this, R.string.picker_map_pick_my_wait, Toast.LENGTH_SHORT).show();
                    } else {
                        buildAlertMessageNoGps();
                        accuracyView.setText(R.string.picker_map_gps_off);
                    }
                }
            }
        });

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER )
                || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {
            // okay navigation is enabled
        }else {
            buildAlertMessageNoGps();
            accuracyView.setText(R.string.picker_map_gps_off);
        }




        controllers = findViewById(R.id.controllers);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        select = findViewById(R.id.select);
        select.setEnabled(false);
        // findViewById(R.id.select_text).setEnabled(false);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                LatLng geoData = currentPick.getPosition();
                returnIntent.putExtra("latitude", geoData.latitude);
                returnIntent.putExtra("longitude", geoData.longitude);
                if(currentPickedAddress!=null)
                    if(currentPickedAddress.getMaxAddressLineIndex()>0)
                        returnIntent.putExtra("street", currentPickedAddress.getAddressLine(0));
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        /*View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    private int defaultMapHolderSize = 0;
    protected void togglePlacesList() {
        if (listHolder.getVisibility() == View.GONE) {
            showPlacesList();
        } else {
            hidePlacesList();
        }
    }

    private boolean hiding;
    private void hidePlacesList() {
        if(currentPick!=null){

        }else {
            if (listHolder.getVisibility() == View.GONE || hiding) {
                return;
            }
        }

        fullSizeButton.setEnabled(false);
        hiding = true;
        float endSize;// = findViewById(R.id.container).getHeight();

        int currentMapHolderSize;
        if(OrientationHelper.getScreenOrientation(getWindowManager())==OrientationHelper.LANDSCAPE){
            endSize = findViewById(R.id.container).getWidth();
            currentMapHolderSize = mapHolder.getWidth();
        }else {
            endSize = findViewById(R.id.container).getHeight();
            if(currentPick!=null) {
                endSize -= getResources().getDimension(R.dimen.picker_map_controllers_height);
            }
            currentMapHolderSize = mapHolder.getHeight();
        }

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentMapHolderSize, endSize);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                if(OrientationHelper.getScreenOrientation(getWindowManager())==OrientationHelper.LANDSCAPE) {
                    mapHolder.getLayoutParams().width = ((Float) valueAnimator.getAnimatedValue()).intValue();
                }else{
                    mapHolder.getLayoutParams().height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                }
                mapHolder.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fullSizeButton.setEnabled(true);
                hiding = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        AlphaAnimation hideAnimation = new AlphaAnimation(1,0);
        hideAnimation.setDuration(300);
        hideAnimation.setInterpolator(new AccelerateInterpolator());
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fullSizeButton.setImageResource(R.drawable.picker_map_halfscreen_icon);
                listHolder.setVisibility(View.GONE);
                valueAnimator.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(listHolder.getVisibility()!=View.GONE) {
            listHolder.startAnimation(hideAnimation);
        }else{
            valueAnimator.start();
        }
    }
    private boolean showing;
    private void showPlacesList() {
        if(listHolder.getVisibility()==View.VISIBLE || showing){
            return;
        }
        showing = true;
        fullSizeButton.setEnabled(false);
        float startSize;
        if(OrientationHelper.getScreenOrientation(getWindowManager())==OrientationHelper.LANDSCAPE){
            startSize = findViewById(R.id.container).getWidth();
        }else
            startSize = findViewById(R.id.container).getHeight();

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(startSize, defaultMapHolderSize);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                if(OrientationHelper.getScreenOrientation(getWindowManager())==OrientationHelper.LANDSCAPE) {
                    mapHolder.getLayoutParams().width = ((Float) valueAnimator.getAnimatedValue()).intValue();
                }else{
                    mapHolder.getLayoutParams().height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                }
                mapHolder.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fullSizeButton.setEnabled(true);
                showing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();

        listHolder.setVisibility(View.VISIBLE);
        fullSizeButton.setImageResource(R.drawable.picker_map_fullscreen_icon);
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
        getMenuInflater().inflate(R.menu.picker_map, menu);
        searchView = (SearchView) menu.getItem(0).getActionView();

        SearchViewHacker.disableCloseButton(searchView);
        SearchViewHacker.disableMagIcon(searchView);
        SearchViewHacker.setIcon(searchView, R.drawable.picker_bar_search);
        SearchViewHacker.setText(searchView, getResources().getColor(R.color.picker_file_searchbox_focused_color));
        SearchViewHacker.setEditText(searchView, R.drawable.picker_search_text_box);
        SearchViewHacker.setHint(searchView, getString(R.string.picker_file_search_query_text), 0, getResources().getColor(R.color.picker_file_searchbox_focused_color), null);
        SearchViewHacker.setCloseIcon(searchView, R.drawable.bar_clear_search);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchPlaces(s);
                hideKeyBoard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideKeyBoard();
                return false;
            }
        });
        return true;
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
            if (lastKnownLocation != null) {
                break;
            }
        }

        if(lastKnownLocation!=null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 14));
            //fetchPlaces(null);
        }
        mMap.setOnMyLocationChangeListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    private void fetchPlaces(String query) {

        mMap.clear();
        if(currentLocation==null){
            Toast.makeText(this, R.string.picker_map_sory_notdefined, Toast.LENGTH_SHORT).show();
            return;
        }
        list.setAdapter(null);
        status.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        fetchingTask = new PlaceFetchingTask(query, 50, currentLocation.getLatitude(), currentLocation.getLongitude()) {
            @Override
            protected void onPostExecute(Object o) {
                Log.i(LOG_TAG, o.toString());
                if(o instanceof ArrayList){
                    places = (ArrayList<MapItem>) o;
                    loading.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                    header.setVisibility(View.VISIBLE);
                    list.setVisibility(View.VISIBLE);
                    if(places.isEmpty()){
                        status.setText(R.string.picker_map_nearby_empty);
                    }else {
                        list.setAdapter(new PlacesAdapter(MapPickerActivity.this, places));
                        showPlacesOnTheMap(places);
                    }
                }else {
                    places = new ArrayList<MapItem>();
                    list.setAdapter(null);
                    header.setVisibility(View.GONE);
                    status.setText(R.string.picker_internalerror);
                    Toast.makeText(MapPickerActivity.this, o.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        fetchingTask.execute();
    }

    void hideKeyBoard(){
        searchView.clearFocus();
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View focusedView = this.getCurrentFocus();
        if(focusedView!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void showPlacesOnTheMap(ArrayList<MapItem> array) {
        if (currentPick == null) {
            mMap.clear();
            markers = new HashMap<String, Marker>();
            for (MapItem mapItem : array) {
                markers.put(mapItem.id,
                        mMap.addMarker(new MarkerOptions()
                                        .position(mapItem.getLatLng())
                                                // .title(mapItem.name)
                                        .draggable(false)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.picker_map_marker))
                        ));
            }
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

        if(currentLocation==null){
            // do we need to attach our location on the start?
            this.currentLocation = location;
            fetchPlaces(null);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
        }
        this.currentLocation = location;;
        accuracyView.setText(getString(R.string.picker_map_pick_my_accuracy, (int) currentLocation.getAccuracy()));
        Log.d("Location changed", location.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        MapItem mapItem = (MapItem) adapterView.getItemAtPosition(position);
        if(list.getCheckedItemPosition()!=position){

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapItem.getLatLng().latitude, mapItem.getLatLng().longitude),
                    16));
            list.setItemChecked(position, true);
            list.smoothScrollToPosition(position);
        }else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("latitude", mapItem.getLatLng().latitude);
            returnIntent.putExtra("longitude", mapItem.getLatLng().longitude);
            returnIntent.putExtra("street", mapItem.vicinity);
            returnIntent.putExtra("place", mapItem.name);

            setResult(RESULT_OK, returnIntent);
            finish();
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {


        if(currentPick==null) {
            mMap.clear();
            MarkerOptions currentPickOptions = new MarkerOptions()
                    .draggable(true)
                    .position(latLng);
            currentPick = mMap.addMarker(currentPickOptions);
        }else{
            currentPick.setPosition(latLng);
        }

        showMapCurrentPin();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(currentPick!=null)
            currentPick.remove();
        String placeId = null;
        for (Map.Entry<String, Marker> markerIterator : markers.entrySet()) {
            if(markerIterator.getValue().equals(marker)){
                placeId = markerIterator.getKey();
                break;
            }
        }
        int position = -1;
        for (int i = 0; i < places.size(); i++) {
            MapItem place = places.get(i);
            if(place.id.equals(placeId)){
                position = i;
                break;
            }
        }
        if(position!=-1) {
                list.setItemChecked(position, true);
                list.smoothScrollToPosition(position);
            if(listHolder.getVisibility()==View.GONE) {
                togglePlacesList();
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(marker.getPosition().latitude, marker.getPosition().longitude),
                16));

        //currentPick = marker;
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        switch (i){
            case SCROLL_STATE_TOUCH_SCROLL:
                hideKeyBoard();
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {

    }

    public void showMapCurrentPin(){

        // todo animate

        fullSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPick = null;
                controllers.setVisibility(View.GONE);
                showPlacesList();
                showPlacesOnTheMap(places);
                fullSizeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        togglePlacesList();
                    }
                });
            }
        });
        hidePlacesList();
        controllers.setVisibility(View.VISIBLE);
        select.setEnabled(true);
        title.setText(R.string.picker_loading);
        subtitle.setText("");
        AddressTask task = new AddressTask(this, currentPick.getPosition()) {
            @Override
            protected void onPostExecute(Address s) {

                currentPickedAddress = s;
                if(s!=null) {
                    if(s.getMaxAddressLineIndex()>0){
                        title.setText(s.getAddressLine(0));
                    }
                    if(s.getLocality()!=null){
                        subtitle.setText(s.getLocality() + ", " + s.getCountryName());
                    }else{
                        if (s.getCountryName() != null) {
                            subtitle.setText(s.getCountryName());
                        }
                    }
                }else{
                    title.setText(R.string.picker_empty);
                    subtitle.setText("");
                }
            }
        };
        task.execute();

    }

    private void hideMapCurrentPin() {

    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(currentPick!=null){
            // todo hide current pick?
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }
}