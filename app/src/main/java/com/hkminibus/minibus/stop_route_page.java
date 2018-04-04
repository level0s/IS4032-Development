package com.hkminibus.minibus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by Jasmine on 27/3/2018.
 */

public class stop_route_page extends Fragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    MapFragment mapFragment;
    Button arrow;

    private GoogleMap mMap;

    private static final String TAG="stopRoute";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view =  inflater.inflate(R.layout.stop_route_fragment, container, false);
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);

        arrow = (Button) view.findViewById(R.id.button_arrow);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.stop_list);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));


        final StopAdapter mStopAdapter = new StopAdapter(getActivity(), stop_main.CStopList);
        mRecyclerView.setAdapter(mStopAdapter);


        return view;
    }
}
