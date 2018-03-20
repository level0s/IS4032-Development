package com.hkminibus.minibus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.Scroller;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

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
    EditText editStart;
    EditText editEnd;
    Button searchButton;
    String Start;
    String End;
    ArrayList allDistrict = new ArrayList();
    ArrayList sDistrict = new ArrayList();
    ArrayList eDistrict = new ArrayList();
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

    RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), mRouteData);

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refDistrict = database.getReference("District");
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


        editStart = (EditText) view.findViewById(R.id.editStart);
        editEnd = (EditText) view.findViewById(R.id.editEnd);
        //searchButton = (Button) view.findViewById(R.id.searchButton);  //fml

        //mLinearLayoutManager.setReverseLayout(true);
        //mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));


        final RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), mRouteData);
        mRecyclerView.setAdapter(mRouteAdapter);


        final Query districtQuery = refDistrict;
        districtQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sDistrict.clear();
                eDistrict.clear();
                allDistrict.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    allDistrict.add(ds.getValue());
                }
                sDistrict.addAll(allDistrict);
                eDistrict.addAll(allDistrict);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        editStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String tempStart = editable.toString();
                if (tempStart.matches("")) {
                    //default: start=current location
                    sDistrict.clear();
                    sDistrict.add("Current location");
                } else {
                    //add filtered locatioins to array
                    sDistrict.clear();
                    for (Object d : allDistrict) {
                        //if the existing elements contains the search input
                        if (d.toString().contains(tempStart)) {
                            sDistrict.add(d);
                        }
                    }
                }
            }
        });
        editStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("請選擇起點");
                    if (sDistrict.isEmpty()){
                        builder.setMessage("找不到地點");
                    } else {
                        builder.setItems((CharSequence[]) sDistrict.toArray(new String[sDistrict.size()]),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           Start = (String)sDistrict.get(which);
                           editStart.setText(Start);
                        }
                    });}
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                }
            }
        });

        editEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String tempEnd = editable.toString();
                if (tempEnd.matches("")) {
                    eDistrict.clear();
                    eDistrict.addAll(allDistrict);
                } else {
                    //add filtered locatioins to array
                    eDistrict.clear();
                    for (Object d : allDistrict) {
                        //if the existing elements contains the search input
                        if (d.toString().contains(tempEnd)) {
                            eDistrict.add(d);
                        }
                    }
                }
            }
        });
        editEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("請選擇目的地");
                    if (eDistrict.isEmpty()){
                        builder.setMessage("找不到地點");
                    } else {
                        builder.setItems((CharSequence[]) eDistrict.toArray(new String[eDistrict.size()]),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                End = (String) eDistrict.get(which); //fml
                                editEnd.setText(End);
                            }
                        });}
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                }
            }
        });

        /*final Query routeQuery = mRef.orderByChild("mRouteNo");
        routeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRouteData.clear();
                allRouteData.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    route_data mRoute = ds.getValue(route_data.class);

                    allRouteData.add(mRoute);

                }
                mRouteData.addAll(allRouteData);
                Log.d("aaaa", "ddddd");
                mRouteAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                mRouteData.clear();
                //Perform search in DB with Start and End, add into mRouteData
                mRouteAdapter.notifyDataSetChanged();
            }
        });*/

        return view;
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

