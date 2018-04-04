package com.hkminibus.minibus;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jasmine on 27/3/2018.
 */

public class stop_ttb extends Fragment {
    private static final String TAG="stopTTB";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =  inflater.inflate(R.layout.stop_ttb_fragment, container, false);


        return view;
    }

}
