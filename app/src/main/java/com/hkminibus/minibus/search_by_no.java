package com.hkminibus.minibus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    EditText editText;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        View view =  inflater.inflate(R.layout.search_by_no_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.route_list);


        editText = (EditText) view.findViewById(R.id.editText);


        //mLinearLayoutManager.setReverseLayout(true);
        //mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));


        final RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), MainActivity.mRouteData);
        mRecyclerView.setAdapter(mRouteAdapter);

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
                    MainActivity.mRouteData.clear();
                    MainActivity.mRouteData.addAll(MainActivity.allRouteData);
                    mRouteAdapter.notifyDataSetChanged();
                    Log.d("dd","have");
                } else {
                    filter(tempText);
                    mRouteAdapter.notifyDataSetChanged();
                }



            }
        });

        //mLinearLayoutManager.setReverseLayout(true);
        //mLinearLayoutManager.setStackFromEnd(true);
        return view;
    }

    private void filter(String text) {
        //new array list that will hold the filtered data


        //looping through existing elements
        MainActivity.mRouteData.clear();

        for (route_data s : MainActivity.allRouteData) {
            //if the existing elements contains the search input
            if (s.getmRouteNo().contains(text.toString().toUpperCase()) || s.getmRouteName().contains(text.toString().toUpperCase())) {

                //adding the element to filtered list
                MainActivity.mRouteData.add(s);

            }
            Log.d("ddddd","visited");
         }


        //calling a method of the adapter class and passing the filtered list
        //mLinearLayoutManager.removeAllViews();
        //mRouteAdapter.filterList(mRouteData);



        //mRouteAdapter.notifyDataSetChanged();
    }


}

