package com.hkminibus.minibus;

/**
 * Created by Jasmine on 3/4/2018.
 */

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.location.Location.distanceBetween;
import static com.hkminibus.minibus.SplashScreen.database;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    public List<stop_data> mStopList;
    private Context mContext;
    int currentNo;
    //private OnItemClickListener mOnItemClickListener;

    /** *ViewHolder = RouteViewHolder*/
    class StopViewHolder extends RecyclerView.ViewHolder {

        TextView mStopName, mIcon, waitingNo, bus16, bus19, time1, time2, full1, full2;
        ImageButton btn_arrow, btn_waitingIcon;
        ConstraintLayout layout;

        StopViewHolder(View itemView) {
            super(itemView);
            btn_arrow = (ImageButton) itemView.findViewById(R.id.button_arrow);
            mStopName = (TextView) itemView.findViewById(R.id.stop_name);
            mIcon = (TextView) itemView.findViewById(R.id.icon);
            bus16 = (TextView) itemView.findViewById(R.id.type1);
            bus19 = (TextView) itemView.findViewById(R.id.type2);
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
    //負責把 Dataset 裡面的資料，轉成 view 給 RecyclerView 顯示
    public StopAdapter (Context mContext, List<stop_data> mStopList) {
        this.mStopList = mStopList;
        this.mContext = mContext;

    }

    //建立 view，並將 view 轉成 ViewHolder
    //创建ChildView
    @Override
    public StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stop_recyclerview,
                parent, false);
        return new StopViewHolder(view);
    }

    //将数据绑定到每一个childView中
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
                        Log.d("Please", "cg");

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
                            Log.d("positionofroute M2", String.valueOf(stop_main.routeID_no)+ stop_main.CRouteData);
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

    }

    //得到child的数量
    @Override
    public int getItemCount() {
        return mStopList.size();
    }

}