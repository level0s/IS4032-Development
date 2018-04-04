package com.hkminibus.minibus;

/**
 * Created by Jasmine on 3/4/2018.
 */

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    private List<stop_data> mStopList;
    private Context mContext;
    //private OnItemClickListener mOnItemClickListener;

    /** *ViewHolder = RouteViewHolder*/
    class StopViewHolder extends RecyclerView.ViewHolder {

        TextView mStopName, mIcon, waitingNo, bus16, bus19, time1, time2;
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
        stop_data mStop = mStopList.get(position);
        holder.setValues(mStop);
        //set the waiting no as 0
        holder.waitingNo.setText("0");

        //Click waitingbutton to make the no +1
        holder.btn_waitingIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Integer currentNo = Integer.parseInt(holder.waitingNo.getText().toString());
                currentNo = currentNo+1;
                holder.waitingNo.setText(currentNo.toString());
            }
        });

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

    }
    //得到child的数量
    @Override
    public int getItemCount() {
        return mStopList.size();
    }


}