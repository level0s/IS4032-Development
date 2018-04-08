package com.hkminibus.minibus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    public static List<route_data> mRouteDataNo = new ArrayList<>();
    RecyclerView mRecyclerView;
    EditText editText;
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =  inflater.inflate(R.layout.search_by_no_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.route_list);

        mRouteDataNo.addAll(MainActivity.allRouteData);

        editText = (EditText) view.findViewById(R.id.editText);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerDecoration(this.getContext(),DividerDecoration.VERTICAL_LIST));


        final RouteAdapter mRouteAdapter = new RouteAdapter(getActivity(), mRouteDataNo);
        mRecyclerView.setAdapter(mRouteAdapter);



        /** *Select the recyclerView **/
        mRouteAdapter.setOnItemClickListener(new RouteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("C", "YY");
                Intent i = new Intent(getActivity(),stop_main.class);
                //i.putExtra("CRouteID", MainActivity.mRouteData.get(position).getmRouteID());
                //i.putExtra("CRouteNo", MainActivity.mRouteData.get(position).getmRouteNo());
                //i.putExtra("CRouteName", MainActivity.mRouteData.get(position).getmRouteName());
                Bundle bundle = new Bundle();
                bundle.putParcelable("CRouteData", mRouteDataNo.get(position));// 序列化
                i.putExtras(bundle);// 发送数据
                startActivity(i);

            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String tempText = editable.toString();

                if (tempText.matches("")) {
                    mRouteDataNo.clear();
                    mRouteDataNo.addAll(MainActivity.allRouteData);
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
        //looping through existing elements
        mRouteDataNo.clear();

        for (route_data s : MainActivity.allRouteData) {
            //if the existing elements contains the search input
            if (s.getmRouteNo().contains(text.toString().toUpperCase()) || s.getmRouteName().contains(text.toString().toUpperCase())) {
                //adding the element to filtered list
                mRouteDataNo.add(s);
            }

         }
    }

}

