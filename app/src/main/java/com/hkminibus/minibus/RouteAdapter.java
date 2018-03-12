package com.hkminibus.minibus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class RouteAdapter extends RecyclerView.Adapter<RouteViewHolder> {

    private List<route_data> mRouteData;
    private Context mContext;

    public RouteAdapter(Context mContext, List<route_data> mRouteData) {
        this.mRouteData = mRouteData;
        this.mContext = mContext;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_route,
                parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RouteViewHolder holder, int position) {
        holder.mRouteNo.setText(mRouteData.get(position).getmRouteNo());
        holder.mRouteName.setText(mRouteData.get(position).getmRouteName());

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

    @Override
    public int getItemCount() {
        return mRouteData.size();
    }
}

class RouteViewHolder extends RecyclerView.ViewHolder {

    TextView mRouteNo;
    TextView mRouteName;
    RelativeLayout mLayout;

    RouteViewHolder(View itemView) {
        super(itemView);

        mRouteNo = (TextView) itemView.findViewById(R.id.route_no);
        mRouteName = (TextView) itemView.findViewById(R.id.route_name);

    }
}
