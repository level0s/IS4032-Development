package com.hkminibus.minibus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import java.util.List;


public class search_by_no extends Fragment {
    private static final String TAG="SearchByNo";
    RecyclerView mRecyclerView;
    List<route_data> mRouteData = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        View view =  inflater.inflate(R.layout.search_by_no_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.route_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        /*route_data mRoute = new route_data("81", "Tsuen Wan to Shek Wai Kok");
        mRouteData.add(mRoute);
        mRoute = new route_data("94", "Shek Wai Kok to Kwai Fong");
        mRouteData.add(mRoute);
        mRoute = new route_data("312", "Lei Muk Shu to Tsing Yi");
        mRouteData.add(mRoute);
        mRoute = new route_data("409", "Tsuen Wan to Tsing Yi");
        mRouteData.add(mRoute);
        mRoute = new route_data("409S", "Tsuen Wan to Tsing Yi");
        mRouteData.add(mRoute);
        mRoute = new route_data("95m", "Tsuen Wan to Tsuen Wan Centre");
        mRouteData.add(mRoute);*/
        final RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), mRouteData);
        mRecyclerView.setAdapter(mRouteAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Route");

       //this is for sorting by number when we tend to use the distance base should change it
        Query query = mRef.orderByChild("mRouteNo");


        query.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRouteData.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                   route_data mRoute = ds.getValue(route_data.class);

                  //  route_data mRoute = new route_data("81", "Tsuen Wan to Shek Wai Kok");
                    mRouteData.add(mRoute);


                }

                mRouteAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }


}

