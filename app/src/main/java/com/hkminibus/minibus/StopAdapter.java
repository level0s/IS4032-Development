package com.hkminibus.minibus;

/**
 * Created by Jasmine on 3/4/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    private List<stop_data> mStopList;
    private Context mContext;
    //private OnItemClickListener mOnItemClickListener;

    /** *ViewHolder = RouteViewHolder*/
    class StopViewHolder extends RecyclerView.ViewHolder {

        TextView mStopName, mIcon;
        ImageButton arrow;
        RecyclerView time;

        StopViewHolder(View itemView) {
            super(itemView);
            arrow = (ImageButton) itemView.findViewById(R.id.button_arrow);
            mStopName = (TextView) itemView.findViewById(R.id.stop_name);
            mIcon = (TextView) itemView.findViewById(R.id.icon);
            time = (RecyclerView) itemView.findViewById(R.id.time);
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
        stop_data mStop = mStopList.get(position);
        holder.setValues(mStop);

    }
    //得到child的数量
    @Override
    public int getItemCount() {
        return mStopList.size();
    }


}