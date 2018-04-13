package com.hkminibus.minibus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
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

    private OnItemClickListener mOnItemClickListener;

    /** *ViewHolder = RouteViewHolder*/
    //no public and final before luke change
    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView mRouteNo;
        TextView mRouteName;
        TextView mRouteIcon;
        ConstraintLayout mLayout;

        RouteViewHolder(View itemView) {
            super(itemView);
            mRouteNo = (TextView) itemView.findViewById(R.id.route_no);
            mRouteName = (TextView) itemView.findViewById(R.id.route_name);
            mRouteIcon = (TextView) itemView.findViewById(R.id.route_icon);
            mLayout = (ConstraintLayout) itemView.findViewById(R.id.route_layout) ;
        }
        public void setValues(route_data mRouteData) {
            mRouteName.setText(mRouteData.getmRouteName());
            mRouteNo.setText(mRouteData.getmRouteNo());
        }
    }
    /** *adapter = Route Adapter*/
    //負責把 Dataset 裡面的資料，轉成 view 給 RecyclerView 顯示
    public RouteAdapter(Context mContext, List<route_data> mRouteData) {
        this.mRouteData = mRouteData;
        this.mContext = mContext;
    }

    //建立 view，並將 view 轉成 ViewHolder
    //创建ChildView
    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_route,
                parent, false);
        return new RouteViewHolder(view);
    }
    //put routedata 顯示在 view
    //将数据绑定到每一个childView中
    @Override
    public void onBindViewHolder(final RouteViewHolder holder, final int position) {
        //holder.mRouteNo.setText(mRouteData.get(position).getmRouteNo());
        //holder.mRouteName.setText(mRouteData.get(position).getmRouteName());

        route_data mRoute = mRouteData.get(position);
        holder.setValues(mRoute);

        //if have RouteNo, which mean it is green minibus
        if (mRoute.getType().equals("red")) {
            holder.mRouteIcon.setBackgroundResource(R.mipmap.mini_red);
            holder.mRouteName.setBackgroundColor(Color.parseColor("#9C1F25"));
        } else if (mRoute.getType().equals("green")) {
            holder.mRouteIcon.setBackgroundResource(R.mipmap.mini_green);
            holder.mRouteName.setBackgroundColor(Color.parseColor("#1C7059"));
        }


        // item click
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }
    //得到child的数量
    @Override
    public int getItemCount() {
        return mRouteData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}