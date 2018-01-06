package com.jundger.carservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.domain.ServicePoint;

import java.util.List;

/**
 * Created by 14246 on 2018/1/5.
 */

public class ServicePointAdapter extends RecyclerView.Adapter<ServicePointAdapter.ViewHolder> {
    private Context mContext;

    private List<ServicePoint> mServicePointList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View shopView;
        ImageView imageView;
        TextView name_tv, score_tv, distance_tv, evaNum_tv, address_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            shopView = itemView;
            imageView = itemView.findViewById(R.id.shop_img_id_tv);
            name_tv = itemView.findViewById(R.id.shop_name_tv);
            score_tv = itemView.findViewById(R.id.shop_score_tv);
            distance_tv = itemView.findViewById(R.id.shop_distance_tv);
            evaNum_tv = itemView.findViewById(R.id.evaluation_count_tv);
            address_tv = itemView.findViewById(R.id.shop_address_tv);
        }
    }

    public ServicePointAdapter(List<ServicePoint> mServicePointList) {
        this.mServicePointList = mServicePointList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service_point, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.shopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                ServicePoint shop = mServicePointList.get(position);
                Toast.makeText(view.getContext(), "子项：" + shop.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                ServicePoint shop = mServicePointList.get(position);
                Toast.makeText(view.getContext(), "图片：" + shop.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServicePoint servicePoint = mServicePointList.get(position);
        holder.imageView.setImageResource(servicePoint.getImageId());
        holder.name_tv.setText(servicePoint.getName());
        holder.score_tv.setText(String.valueOf(servicePoint.getScore()));
        holder.distance_tv.setText(String.valueOf(servicePoint.getDistance()));
        holder.evaNum_tv.setText(String.valueOf(servicePoint.getEvaluationCount()));
        holder.address_tv.setText(servicePoint.getAddress());
    }

    @Override
    public int getItemCount() {
        return mServicePointList.size();
    }
}
