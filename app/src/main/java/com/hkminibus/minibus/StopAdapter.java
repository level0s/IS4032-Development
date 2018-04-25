package com.hkminibus.minibus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.location.Location.distanceBetween;
import static com.hkminibus.minibus.SplashScreen.database;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    public List<stop_data> mStopList;
    private Context mContext;
    int currentNo;
    public static onclick_data onClicked;
    //private OnItemClickListener mOnItemClickListener;

    List<driving_mini_data> cRouteDriving = new ArrayList<>();
    List<driving_mini_data> tmpCRD = new ArrayList<>();
    List<Integer> time = new ArrayList<>();
    List<Boolean> full = new ArrayList<>();
    List<String> type = new ArrayList<>();


    /** *ViewHolder = RouteViewHolder*/
    class StopViewHolder extends RecyclerView.ViewHolder {

        TextView mStopName, mIcon, waitingNo, type1, type2, time1, time2, full1, full2;
        ImageButton btn_arrow, btn_waitingIcon;
        ConstraintLayout layout;

        StopViewHolder(View itemView) {
            super(itemView);
            btn_arrow = (ImageButton) itemView.findViewById(R.id.button_arrow);
            mStopName = (TextView) itemView.findViewById(R.id.stop_name);
            mIcon = (TextView) itemView.findViewById(R.id.icon);
            type1 = (TextView) itemView.findViewById(R.id.type1);
            type2 = (TextView) itemView.findViewById(R.id.type2);
            time1 = (TextView) itemView.findViewById(R.id.time1);
            time2 = (TextView) itemView.findViewById(R.id.time2);
            full1 = (TextView) itemView.findViewById(R.id.FULL1);
            full2 = (TextView) itemView.findViewById(R.id.FULL2);
            btn_waitingIcon = (ImageButton) itemView.findViewById(R.id.waitingPerson_icon);
            waitingNo = (TextView) itemView.findViewById(R.id.waitingPerson_no);
            layout= (ConstraintLayout) itemView.findViewById(R.id.time_layout);


        }
        public void setValues(stop_data mStopList) {
            mStopName.setText(mStopList.getName());

        }
    }
    /** *adapter = Route Adapter*/
    public StopAdapter (Context mContext, List<stop_data> mStopList) {
        this.mStopList = mStopList;
        this.mContext = mContext;

    }

    @Override
    public StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stop_recyclerview,
                parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StopViewHolder holder, final int position) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final Map<String, Object> RankUpdates = new HashMap<>();

        //set the waiting no and stopname as database shown
        final stop_data mStop = mStopList.get(position);
        holder.mStopName.setText(mStop.getName());
        holder.waitingNo.setText(String.valueOf(mStop.getRank()));
        int s = position+1;
        final String stopID = String.valueOf(s < 10 ? "0" : "") + s;

        //set the time_layout visible after clicking arrow_button
        holder.btn_arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(holder.layout.getVisibility()==View.VISIBLE){
                    holder.layout.setVisibility(View.GONE);
                }else{
                    holder.layout.setVisibility(View.VISIBLE);
                }
            }
        });

        //Click Rank button to make the no +1
        //Constraint1: near the bus stop, including 500000m (testing)
        //Constraint2: cannot click the button more than 1 time
        holder.btn_waitingIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                float[] dist = new float[1];
                distanceBetween(mStop.getLatitude(), mStop.getLongitude(), stop_route_page.currentLat, stop_route_page.currentLng, dist);
                DatabaseReference route_stop = mRef.child("Stop").child(stop_main.routeID).child(stop_main.routeID + "_" + stopID);

                if (dist[0] < 500000) {
                    if (stop_main.clicked ==0) {
                        stop_main.clickedPosition = position;
                        int Rank = Integer.parseInt(holder.waitingNo.getText().toString());
                        Rank = Rank + 1;

                        //Update firebase the new rank value
                        RankUpdates.put("rank", Rank);
                        route_stop.updateChildren(RankUpdates);
                        notifyDataSetChanged();

                        //Reset the icon waiting No
                        holder.waitingNo.setText(String.valueOf(mStop.getRank()));

                        //indicate that it hs benn clicked
                        stop_main.clicked = 1;

                        if (stop_main.allOnCLicked == null || !containsId(stop_main.allOnCLicked,stop_main.CRouteData.getmRouteID())) {

                            onClicked = new onclick_data();
                            onClicked.setmRouteID(stop_main.CRouteData.getmRouteID());
                            onClicked.setPosition(stop_main.clickedPosition);
                            onClicked.setClicked(stop_main.clicked);
                            stop_main.allOnCLicked.add(onClicked);
                            for (onclick_data abc : stop_main.allOnCLicked) {
                                System.out.println(abc.getmRouteID());
                                System.out.println(abc.getClicked());
                                System.out.println(abc.getPosition());
                            }


                        }else {
                            for (onclick_data s : stop_main.allOnCLicked) {
                                //if the existing elements contains the search input
                                if (s.getmRouteID().contains(stop_main.CRouteData.getmRouteID())) {
                                    //adding the element to filtered list
                                   s.setClicked(stop_main.clicked);
                                   s.setPosition(stop_main.clickedPosition);
                                }
                            }
                            for (onclick_data abc : stop_main.allOnCLicked) {
                                System.out.println(abc.getmRouteID());
                                System.out.println(abc.getClicked());
                                System.out.println(abc.getPosition());
                            }
                        }

                    }
                    else{
                        Toast.makeText(mContext, "你已排隊,只可以排隊一次,請長接這按鈕取消排隊", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(mContext, "你的位置不在這站附近", Toast.LENGTH_SHORT).show();
                    }
        }});

        //Click long click to delete ranking
        holder.btn_waitingIcon.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                if (stop_main.clicked == 1) {
                    if (stop_main.clickedPosition == position) {
                        DatabaseReference route_stop = mRef.child("Stop").child(stop_main.routeID).child(stop_main.routeID + "_" + stopID);
                        Toast.makeText(mContext, "你已取消排隊", Toast.LENGTH_SHORT).show();

                        int Rank = Integer.parseInt(holder.waitingNo.getText().toString());
                        Rank = Rank - 1;

                        //Update firebase the new rank value
                        RankUpdates.put("rank", Rank);
                        route_stop.updateChildren(RankUpdates);
                        notifyDataSetChanged();

                        //Reset the icon waiting No
                        holder.waitingNo.setText(String.valueOf(mStop.getRank()));

                        stop_main.clicked = 0;
                        stop_main.clickedPosition= stop_main.resetPosition;
                        for (onclick_data s : stop_main.allOnCLicked) {
                            //if the existing elements contains the search input
                            if (s.getmRouteID().contains(stop_main.CRouteData.getmRouteID())) {
                                //adding the element to filtered list
                                s.setClicked(stop_main.clicked);
                                s.setPosition(stop_main.clickedPosition);
                            }
                        }

                    }
                    else{
                        Toast.makeText(mContext, "請長按你已排隊的站來取消排隊", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        //If clicked the button, the button will change color to red
        //If unclicked, it back to grey
        if (stop_main.clicked == 0){
            holder.btn_waitingIcon.setImageResource(R.drawable.grey_wait);
            holder.waitingNo.setTextColor(Color.parseColor("#696969"));
        }
        else if (stop_main.clicked ==1){
            if (stop_main.clickedPosition == position){
            holder.btn_waitingIcon.setImageResource(R.drawable.waiting_ppl_icon);
            holder.waitingNo.setTextColor(Color.parseColor("#9C1F25"));
            }
            else {
                holder.btn_waitingIcon.setImageResource(R.drawable.grey_wait);
                holder.waitingNo.setTextColor(Color.parseColor("#696969"));
            }
        }

        //update the list
        mRef.child("Stop/" + stop_main.routeID).addChildEventListener(new ChildEventListener()  {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                stop_data update = dataSnapshot.getValue(stop_data.class);
                if (previousChildName == null) {
                        stop_main.CRouteData.getmStopList().set(0, update);
                    }
                 else {
                        String[] token = previousChildName.split("_");
                        int previoudStopID = Integer.parseInt(token[1]) + 1;
                        String stopid = String.valueOf(previoudStopID < 10 ? "0" : "") + previoudStopID;
                        String currentChildName = token[0] + "_" + stopid;

                        if (currentChildName.equals(stop_main.routeID + "_" + stopID)) {
                            stop_main.CRouteData.getmStopList().set(position, update);
                        }
                    }
                notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName){}
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName){}
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        mRef.child("Driving").addValueEventListener(new ValueEventListener()  {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cRouteDriving.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    driving_mini_data d = ds.getValue(driving_mini_data.class);
                    if (d.getmRouteName().matches(stop_main.CRouteData.getmRouteName())&&d.isDriving()) {
                        cRouteDriving.add(d);
                    }
                }

                time.clear();
                full.clear();
                for (driving_mini_data d : cRouteDriving) {
                    if(!passed(d,position)){
                        LatLng origin = new LatLng(d.getLat(), d.getLng());
                        LatLng destination = new LatLng(mStop.getLatitude(), mStop.getLongitude());
                        DateTime now = new DateTime();
                        DirectionsResult result = null;
                        try {
                            result = DirectionsApi.newRequest(getGeoContext())
                                    .mode(TravelMode.DRIVING).origin(origin)
                                    .destination(destination).departureTime(now)
                                    .await();
                        } catch (ApiException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (result != null) {
                            time.add((int) (result.routes[0].legs[0].duration.inSeconds / 60));
                            full.add(d.isFull());
                            type.add(d.getCarSize());
                        }
                    }
                }
                switch (time.size()) {
                    case 0:
                        holder.time1.setText("目前沒有服務");
                        holder.time2.setText("");
                        holder.type1.setBackgroundResource(R.drawable.blank);
                        holder.full1.setText("");
                        holder.full2.setText("");
                        holder.type2.setBackgroundResource(R.drawable.blank);
                        break;
                    case 1:
                        int min = time.get(0);
                        boolean minFull = full.get(0);
                        String minType = type.get(0);
                        for (int i = 1; i < time.size(); i++) {
                            if (time.get(i) < min) {
                                min = time.get(i);
                                minFull = full.get(i);
                                minType = type.get(i);
                            }
                        }
                        holder.time1.setText("還有" + min + "分鐘");
                        if (minFull) {
                            holder.full1.setText("客滿");
                        } else {
                            holder.full1.setText("");
                        }
                        if (minType.matches("16")) {
                            holder.type1.setBackgroundResource(R.mipmap.bus16_icon);
                        } else if (minType.matches("19")){
                            holder.type1.setBackgroundResource(R.mipmap.bus19_icon);
                        }
                        holder.time2.setText("");
                        holder.full2.setText("");
                        holder.type2.setBackgroundResource(R.drawable.blank);
                        break;
                    default:
                        int min1 = time.get(0);
                        boolean min1Full = full.get(0);
                        String min1Type = type.get(0);
                        int min2 = time.get(1);
                        boolean min2Full = full.get(1);
                        String min2Type = type.get(1);
                        if(time.get(1)<time.get(0)){
                            min1 = time.get(1);
                            min1Full = full.get(1);
                            min1Type = type.get(1);
                            min2 = time.get(0);
                            min2Full = full.get(0);
                            min2Type = type.get(0);
                        }
                        for (int i=2;i<time.size();i++){
                            if (time.get(i) < min1)
                            {
                                min2 = min1;
                                min1 = time.get(i);
                                min2Full = min1Full;
                                min1Full = full.get(i);
                                min2Type = min1Type;
                                min1Type = type.get(i);
                            }else if (time.get(i)<min2){
                                min2 = time.get(i);
                                min2Full = full.get(i);
                                min2Type = type.get(i);
                            }
                        }
                        holder.time1.setText("還有" + min1 + "分鐘");
                        holder.time2.setText("還有" + min2 + "分鐘");
                        if (min1Full) {
                            holder.full1.setText("客滿");
                        } else {
                            holder.full1.setText("");
                        }
                        if (min2Full) {
                            holder.full2.setText("客滿");
                        } else {
                            holder.full2.setText("");
                        }
                        if(min1Type.matches("16")){
                            holder.type1.setBackgroundResource(R.mipmap.bus16_icon);
                        }else if(min1Type.matches("19")){
                            holder.type1.setBackgroundResource(R.mipmap.bus19_icon);
                        }
                        if(min2Type.matches("16")){
                            holder.type2.setBackgroundResource(R.mipmap.bus16_icon);
                        }else if(min2Type.matches("19")){
                            holder.type2.setBackgroundResource(R.mipmap.bus19_icon);
                        }
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public int getItemCount() {
        return mStopList.size();
    }

    public static boolean containsId(List<onclick_data> list, String id) {
        for (onclick_data object : list) {
            System.out.println(object.getmRouteID());
            if (object.getmRouteID().equals(id)) {
                return true;
            }
        }

        return false;
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey("AIzaSyAjAEDSx9VucdwOGv65eo7HgqryPNyAVyA")
                .setConnectTimeout(5, TimeUnit.SECONDS)
                .setReadTimeout(5, TimeUnit.SECONDS)
                .setWriteTimeout(5, TimeUnit.SECONDS);
    }
    private boolean passed(driving_mini_data d, int position){
        boolean passed = false;
        int carPosition = 0;
        for (int i=0;i<mStopList.size();i++) {
            if (mStopList.get(i).getName().matches(d.getStopName())) {
                carPosition = i;
            }
        }
        if(carPosition>position){
            passed = true;
        }
        return passed;
    }
}