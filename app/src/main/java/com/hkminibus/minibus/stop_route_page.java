package com.hkminibus.minibus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by Jasmine on 27/3/2018.
 */

public class stop_route_page extends Fragment{

    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    MapFragment mapFragment;
    Button btn_arrow, btn_waitingIcon;
    TextView waitingNo;

    private GoogleMap mMap;

    private static final String TAG="stopRoute";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view =  inflater.inflate(R.layout.stop_route_fragment, container, false);
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);

        //btn_waitingIcon = (ImageButton) view.findViewById(R.id.waitingPerson_icon);
        //waitingNo = (TextView) view.findViewById(R.id.waitingPerson_no);
        //waitingNo.setText("0");

        //RecyclerView divider
        mRecyclerView = (RecyclerView) view.findViewById(R.id.stop_list);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));

        //RecyclerView Adapter
        final StopAdapter mStopAdapter = new StopAdapter(getActivity(), stop_main.CStopList);
        mRecyclerView.setAdapter(mStopAdapter);

        return view;
    }

}
