package com.hkminibus.minibus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class on_car extends AppCompatActivity implements OnMapReadyCallback {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference mRef = database.getReference();
    final Map<String, Object> off_car_update = new HashMap<>();
    public String key;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView nextStop;
    private ImageButton leaveBtn;
    public static driving_mini_data drivingMinibus;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    //An immutable class that aggregates all camera position parameters such as location, zoom level, tilt angle, and bearing.
    //a object
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is not granted.
    private final LatLng mDefaultLocation = new LatLng(22.352734, 114.1277);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    public Marker marker;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    public KmlLayer kmlLayer;
    double miniBusLat = 0.0;
    double miniBusLng = 0.0;

    private static final String TAG="on_car";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_car);

        //get current route and stop
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        drivingMinibus = getIntent().getParcelableExtra("driving");

        String No = drivingMinibus.getmRouteNo();
        String Name = drivingMinibus.getmRouteName();
        String nextMinibusStop = drivingMinibus.getStopName();

        //Set Route Name as toolbar title
        toolbarTitle = (TextView)findViewById(R.id.current_route);
        toolbarTitle.setText(No + " " + Name);
        nextStop = (TextView)findViewById(R.id.nextStop);
        nextStop.setText(nextMinibusStop);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //set app bar
        toolbar = (Toolbar) findViewById(R.id.toolbar2);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        //SupportMapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Query Driving = mRef.child("Driving");
        Driving.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getValue(driving_mini_data.class).getmPlateNo().equals(drivingMinibus.getmPlateNo())) {
                    if (dataSnapshot.getValue(driving_mini_data.class).isDriving() == drivingMinibus.isDriving()) {
                        drivingMinibus = dataSnapshot.getValue(driving_mini_data.class);
                        key = dataSnapshot.getKey();
                        Log.d("hi", "2");
                        nextStop.setText(drivingMinibus.getStopName());
                        if(drivingMinibus.isNextStop() == true){
                            nextStop.setTextColor(getColor(R.color.red));
                        } else {
                            nextStop.setTextColor(getColor(R.color.grey));
                        }
                        setMarker(drivingMinibus);

                    }
                }
                Log.d("hi","1");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getKey());
                System.out.println(dataSnapshot.getValue(driving_mini_data.class).getmPlateNo());
                System.out.println(drivingMinibus.getmPlateNo());
                System.out.println(drivingMinibus.getLat());
                System.out.println(drivingMinibus.getLng());
                System.out.println(drivingMinibus.getmRouteName());
                if (dataSnapshot.getValue(driving_mini_data.class).getmPlateNo().equals(drivingMinibus.getmPlateNo())) {
                    if (dataSnapshot.getValue(driving_mini_data.class).isDriving() == drivingMinibus.isDriving()) {
                        drivingMinibus = dataSnapshot.getValue(driving_mini_data.class);
                        key = dataSnapshot.getKey();
                        Log.d("hi", "2");
                        nextStop.setText(drivingMinibus.getStopName());
                        if(drivingMinibus.isNextStop() == true){
                            nextStop.setTextColor(getColor(R.color.red));
                        } else {
                            nextStop.setTextColor(getColor(R.color.grey));
                        }
                        setMarker(drivingMinibus);

                    }
                }
                System.out.println(drivingMinibus.getLat());
                System.out.println(drivingMinibus.getLng());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getKey());
                Log.d("hi","3");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getKey());
                Log.d("hi","4");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button button = (Button) findViewById(R.id.off_car);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(on_car.this);
                builder.setTitle("是否確定下一站為下車位置？");

                // Set up the buttons
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        if(drivingMinibus.isNextStop() == true){
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(on_car.this);
                            dlgAlert.setTitle("司機已收到指示謝謝。");
                            dlgAlert.setNeutralButton("謝謝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.cancel();
                                }
                            });
                            dlgAlert.show();

                        } else {
                            DatabaseReference off_car = mRef.child("Driving").child(key);
                            System.out.println(key);
                            Boolean confirm_off_car =true;
                            off_car_update.put("nextStop", confirm_off_car );
                            off_car.updateChildren(off_car_update);
                            Log.d("finished","off_car");

                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(on_car.this);
                            dlgAlert.setTitle("已通知司機");
                            dlgAlert.setNeutralButton("謝謝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.cancel();
                                }
                            });
                            dlgAlert.show();
                        }

                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        leaveBtn = (ImageButton) findViewById(R.id.leaveBtn);
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //callback to save the state when the activity pauses:
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Prompt the user for permission.
       // getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
       // updateLocationUI();
        // Get the current location of the device and set the position of the map.
        //getDeviceLocation();

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(miniBusLat,miniBusLat), DEFAULT_ZOOM));
        Log.d(TAG, "camera chingching.");
        searchKMLFile(drivingMinibus.getmRouteName());
        double lat = drivingMinibus.getLat();
        double lng = drivingMinibus.getLng();
        LatLng location = new LatLng(lat, lng);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_green)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),16));

    }

    private void setMarker(driving_mini_data drivingMinibus) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once

        if (marker != null){
            marker.remove();
        }
        double lat = drivingMinibus.getLat();
        double lng = drivingMinibus.getLng();
        LatLng location = new LatLng(lat, lng);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_green)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),16));
    }
    //Use the fused location provider to find the device's last-known location, then use that location to position the map.
    /*private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            miniBusLat = drivingMinibus.getLat();
                            miniBusLng = drivingMinibus.getLng();
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(miniBusLat, miniBusLng), DEFAULT_ZOOM));
                            Log.d(TAG, "camera should be moved.");
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s getresult", task.getException());
                            //mMap.moveCamera(CameraUpdateFactory
                            //      .newLatLngZoom(mDefaultLocation, 10));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        }
                    }

                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %strycatch", e.getMessage());
        }
    }*/

    /*private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }*/

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
        //updateLocationUI();
    }
    /*
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
               // getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %sui", e.getMessage());
        }
    }*/
    public void searchKMLFile (String mapId) {
        try {
            retrieveFileFromResource(mapId);
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    public void retrieveFileFromResource(String mapId) {
        try {
            Log.d(TAG,mapId);
            if (mapId.equals("旺角砵蘭街->石圍角及象山")){
                KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.m1b, this.getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
            }
            if (mapId.equals("白田邨→旺角東站")){
                KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.m2, this.getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
            }
            if (mapId.equals("柏景灣->旺角東站")){
                KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.m3, this.getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
            }
            if (mapId.equals("")){
                Log.d(TAG, "remove layer");
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(miniBusLat, miniBusLng), 15));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void moveCameraToKml(KmlLayer kmlLayer) {
        //Retrieve the first container in the KML layer
        KmlContainer container = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        container = container.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        KmlPlacemark placemark = container.getPlacemarks().iterator().next();
        //Retrieve a polygon object in a placemark
        KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
        //Create LatLngBounds of the outer coordinates of the polygon
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
            builder.include(latLng);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        width = width-100;
        height = height-200;
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, 1));
        Log.d(TAG,"kml has input");
    }

    @Override
    public void onBackPressed(){
        Log.d("C", "YY");
        finish();
    }
}
