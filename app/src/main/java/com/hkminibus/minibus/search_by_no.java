package com.hkminibus.minibus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        route_data mRoute = new route_data("81", "Tsuen Wan to Shek Wai Kok");
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
        mRouteData.add(mRoute);

        RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), mRouteData);
        mRecyclerView.setAdapter(mRouteAdapter);
        return view;
    }


}
