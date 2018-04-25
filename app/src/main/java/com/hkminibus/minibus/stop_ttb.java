package com.hkminibus.minibus;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class stop_ttb extends Fragment {
    private static final String TAG="stopTTB";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =  inflater.inflate(R.layout.stop_ttb_fragment, container, false);
        TextView MonToFri = (TextView) view.findViewById(R.id.MonToFri);
        TextView Sat = (TextView) view.findViewById(R.id.Sat);
        TextView Sun = (TextView) view.findViewById(R.id.Sun);
        MonToFri.setText(stop_main.CRouteData.getMonToFri());
        Sat.setText(stop_main.CRouteData.getSat());
        Sun.setText(stop_main.CRouteData.getSun());
        return view;
    }

}
