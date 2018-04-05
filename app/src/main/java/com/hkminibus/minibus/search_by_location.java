package com.hkminibus.minibus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup.LayoutParams;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CollapsingToolbarLayout;


import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;

import static android.location.Location.distanceBetween;


//com.google.android.gms.location.locationlistner,googleapiclient.connectioncallbacks, googleapiclient.onconnectionfailedListener
public class search_by_location extends Fragment implements OnMapReadyCallback {
    private static final String TAG="SearchByLocation";
    private boolean isFulllScreen = false;

    RecyclerView mRecyclerView;
    ArrayAdapter locationAdapter;
    AutoCompleteTextView editStart;
    AutoCompleteTextView editEnd;
    ImageButton searchButton;
    ImageButton locationButton;
    location_data start;
    location_data end;
    ArrayList locationName = new ArrayList();
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    double currentLat = 0.0;
    double currentLng = 0.0;

    private GoogleMap mMap;
    //An immutable class that aggregates all camera position parameters such as location, zoom level, tilt angle, and bearing.
    //a object
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(22.352734, 114.1277);
    private static final int DEFAULT_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    // 連線與使用Google Services服務
    //private GoogleApiClient mGoogleApiClient;

   /* private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    static public final int REQUEST_LOCATION_PERMISSION = 1;*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        View view =  inflater.inflate(R.layout.search_by_location_fragment, container, false);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.fakeAppBar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Build the map.
        //SupportMapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.route_list);

        //create location name list for searching
        for (location_data i : MainActivity.allLocation) {
            if(!locationName.contains(i.getName())){locationName.add(i.getName());}
        }

        //set auto complete text view for start point and default at current location
        locationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,locationName);
        editStart = (AutoCompleteTextView) view.findViewById(R.id.editStart);
        editStart.setAdapter(locationAdapter);
        editStart.setThreshold(1);
        editStart.setAdapter(locationAdapter);
        editStart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
            }
        });
        //set auto complete text view for destination
        editEnd = (AutoCompleteTextView) view.findViewById(R.id.editEnd);
        editEnd.setThreshold(1);
        editEnd.setAdapter(locationAdapter);
        editEnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
            }
        });
        searchButton = (ImageButton) view.findViewById(R.id.searchButton);
        locationButton = (ImageButton) view.findViewById(R.id.locationButton);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));

        final RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), MainActivity.mRouteData);
        mRecyclerView.setAdapter(mRouteAdapter);

        locationButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                editStart.setText("目前位置");
            }
        });

        searchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);

                //set start location and end location, then search route.
                if (validateSearch(editStart.getText().toString(),editEnd.getText().toString())) {
                    MainActivity.mRouteData.clear();
                    List<route_data> matchStart = new ArrayList<>();
                    List<Integer> startIndex = new ArrayList<>();
                    for (route_data r : MainActivity.allRouteData) {
                        for (stop_data s : r.getmStopList()) {
                            float[] dist = new float[1];
                            distanceBetween(start.getLatitude(), start.getLongitude(), s.getLatitude(), s.getLongitude(), dist);
                            if (dist[0] < start.getRadius()) {
                                matchStart.add(r);
                                startIndex.add(r.getmStopList().indexOf(s));
                                break;
                            }
                        }
                    }
                    for (route_data r : matchStart) {
                        int j=0;
                        for (int i=startIndex.get(j);i<startIndex.size();i++){
                            float[] dist = new float[1];
                            distanceBetween(end.getLatitude(), end.getLongitude(), r.getmStopList().get(i).getLatitude(), r.getmStopList().get(i).getLongitude(), dist);
                            if (dist[0] < end.getRadius()) {
                                MainActivity.mRouteData.add(r);
                                break;
                            }
                        }
                        j++;
                    }
                    mRouteAdapter.notifyDataSetChanged();
                }
            }
        });

        /** *Select the recyclerView **/
        mRouteAdapter.setOnItemClickListener(new RouteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("C", "YY");
                Intent i = new Intent(getActivity(),stop_main.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("CRouteData", MainActivity.mRouteData.get(position));// 序列化
                i.putExtras(bundle);// 发送数据
                startActivity(i);
            }
        });
        return view;
    }



    //callback to save the state when the activity pauses:
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    //Use the fused location provider to find the device's last-known location, then use that location to position the map.
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                //??
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            currentLat = mLastKnownLocation.getLatitude();
                            currentLng = mLastKnownLocation.getLongitude();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, 10));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
                //Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                //handleNewLocation(location);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    /*private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
    }*/
    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //confirm start/end location are correct
    private Boolean validateSearch(String s, String e){
        boolean S = false;
        if (s.matches("目前位置")){
            S = true;
            start = new landmark_data("Current location",currentLat,currentLng,500);
        }
        else {
            for (location_data i : MainActivity.allLocation) {
                if (i.getName().matches(s)) {
                    S = true;
                    start = i;
                }
            }
        }
        boolean E = false;
        if (!s.matches("目前位置") && e.matches("目前位置")){
            E = true;
            end = new landmark_data("Current location",currentLat,currentLng,500);
        }
        else {
            for (location_data i : MainActivity.allLocation) {
                if (i.getName().matches(e)) {
                    E = true;
                    end = i;
                }
            }
        }
        Boolean valid = S&&E;
        if (valid==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (!S) {
                builder.setTitle("請輸入正確起點");
            }
            else if (!E){
                builder.setTitle("請輸入正確目的地");}
            builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.show();
        }
        return valid;
    }
}



