package com.hkminibus.minibus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.Scroller;
import android.widget.TextView;
import android.content.Context;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;
import java.util.List;


public class search_by_location extends Fragment implements OnMapReadyCallback{
    private static final String TAG="SearchByLocation";
    private boolean isFulllScreen = false;

    RecyclerView mRecyclerView;
    List<route_data> mRouteData = new ArrayList<>();

    List<route_data> allRouteData = new ArrayList<>();
    EditText editText;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference("Route");


    private GoogleMap gMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        View view =  inflater.inflate(R.layout.search_by_location_fragment, container, false);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.route_list);


        editText = (EditText) view.findViewById(R.id.editText);


        //mLinearLayoutManager.setReverseLayout(true);
        //mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));


        final RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), mRouteData);
        mRecyclerView.setAdapter(mRouteAdapter);


        final Query query = mRef.orderByChild("mRouteNo");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRouteData.clear();
                allRouteData.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    route_data mRoute = ds.getValue(route_data.class);

                    allRouteData.add(mRoute);

                }
                mRouteData.addAll(allRouteData);
                Log.d("aaaa","ddddd");
                mRouteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*String tempText = charSequence.toString();


                if (tempText.matches("")) {
                    mRouteData.clear();
                    mRouteData.addAll(allRouteData);
                    mRouteAdapter.notifyDataSetChanged();
                    Log.d("dd","have");
                } else {
                    filter(tempText);
                }*/

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tempText = editable.toString();


                if (tempText.matches("")) {
                    mRouteData.clear();
                    mRouteData.addAll(allRouteData);
                    mRouteAdapter.notifyDataSetChanged();
                    Log.d("dd","have");
                } else {
                    filter(tempText);
                    mRouteAdapter.notifyDataSetChanged();
                }



            }
        });



        return view;
    }

    private void filter(String text) {
        //new array list that will hold the filtered data


        //looping through existing elements
        mRouteData.clear();

        for (route_data s : allRouteData) {
            //if the existing elements contains the search input
            if (s.getmRouteNo().contains(text.toString().toUpperCase()) || s.getmRouteName().contains(text.toString().toUpperCase())) {

                //adding the element to filtered list
                mRouteData.add(s);

            }
            Log.d("ddddd","visited");
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
                ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
                params.height = 900;
                mapFragment.getView().setLayoutParams(params);
                Log.d("testclick","googlemap");
            }
        });

    }






}

