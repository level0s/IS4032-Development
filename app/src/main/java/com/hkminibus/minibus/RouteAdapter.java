package com.hkminibus.minibus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<route_data> mRouteData;
    private Context mContext;

    public RouteAdapter(Context mContext, List<route_data> mRouteData) {
        this.mRouteData = mRouteData;
        this.mContext = mContext;
    }
    //创建ChildView
    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_route,
                parent, false);
        return new RouteViewHolder(view);
    }

    //将数据绑定到每一个childView中
    @Override
    public void onBindViewHolder(final RouteViewHolder holder, int position) {
        //holder.mRouteNo.setText(mRouteData.get(position).getmRouteNo());
        //holder.mRouteName.setText(mRouteData.get(position).getmRouteName());

        route_data mRoute = mRouteData.get(position);
        holder.setValues(mRoute);

        /*Tap and go to another page
                holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(mContext, DetailActivity.class);
                    mIntent.putExtra("sender", holder.mRouteNo.getText().toString());
                    mIntent.putExtra("title", holder.mRouteName.getText().toString());
                    mContext.startActivity(mIntent);
                }
             });*/
    }

    //得到child的数量
    @Override
    public int getItemCount() {
        return mRouteData.size();
    }


    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    /*public void filterList(List<route_data> filterdNames) {

        notifyDataSetChanged();


        Log.d("", "Herewego4");
    }*/
//}

    //no public and final before luke change
    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView mRouteNo;
        TextView mRouteName;

        RelativeLayout mLayout;

        RouteViewHolder(View itemView) {
            super(itemView);
            mRouteNo = (TextView) itemView.findViewById(R.id.route_no);
            mRouteName = (TextView) itemView.findViewById(R.id.route_name);


        }

        public void setValues(route_data mRouteData) {
            mRouteName.setText(mRouteData.getmRouteName());
            mRouteNo.setText(mRouteData.getmRouteNo());

        }
    }
}