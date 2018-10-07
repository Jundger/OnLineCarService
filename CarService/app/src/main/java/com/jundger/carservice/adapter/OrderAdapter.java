package com.jundger.carservice.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jundger.carservice.R;
import com.jundger.carservice.activity.OrderActivity;
import com.jundger.carservice.activity.OrderDetailActivity;
import com.jundger.carservice.bean.json.FaultCode;
import com.jundger.carservice.bean.json.OrderJson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderJson> orderJsonList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView orderno_value_tv;
        TextView order_status_tv;
        ImageView order_enter_iv;
        TextView order_resolver_tv;
        TextView order_distance_tv;
        TextView order_describe_tv;
        TextView order_date_tv;

        private ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            orderno_value_tv = itemView.findViewById(R.id.orderno_value_tv);
            order_status_tv = itemView.findViewById(R.id.order_status_tv);
            order_enter_iv = itemView.findViewById(R.id.order_enter_iv);
            order_resolver_tv = itemView.findViewById(R.id.order_resolver_tv);
            order_distance_tv = itemView.findViewById(R.id.order_distance_tv);
            order_describe_tv = itemView.findViewById(R.id.order_describe_tv);
            order_date_tv = itemView.findViewById(R.id.order_date_tv);
        }
    }

    public OrderAdapter(List<OrderJson> orderJsonList) {
        this.orderJsonList = orderJsonList;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        final OrderJson orderJson = orderJsonList.get(position);

//        Log.i("OrderActivity", "onBindViewHolder: " + new Gson().toJson(orderJson));
        holder.orderno_value_tv.setText(orderJson.getOrderNo());
        String status = "NULL";
        String resolvStatus = orderJson.getResolveStatus();
        if ("WAITTING".equals(resolvStatus)) {
            status = "等待接单";
        } else if ("RUNNING".equals(resolvStatus)){

            status = "正在进行";
        } else if ("FINISH".equals(resolvStatus)){
            status = "已完成";
        }
        holder.order_status_tv.setText(status);
        holder.order_resolver_tv.setText(orderJson.getSiteName());


        double longitude1 = OrderActivity.longitude;
        double latitude1 = OrderActivity.latitude;
        double longitude2 = orderJson.getLongitude();
        double latitude2 = orderJson.getLatitude();
        double distance = algorithm(longitude1,latitude1,longitude2,latitude2)/1000;
        DecimalFormat df = new DecimalFormat("0.00");
        holder.order_distance_tv.setText(String.format("%s km", df.format(distance)));

        StringBuilder sb = new StringBuilder();
        for (FaultCode faultCode : orderJson.getFaultCodeList()) {
            sb.append(faultCode.getDescribe()).append("; ");
        }
        holder.order_describe_tv.setText(sb.toString());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.order_date_tv.setText(sdf.format(orderJson.getCreateTime()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("OrderActivity", "onClick: " + new Gson().toJson(orderJson));
                OrderDetailActivity.launchActivity(view.getContext(), orderJson);
            }
        });
    }

    private static double algorithm(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1); // 纬度
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;//两点纬度之差
        double b = rad(longitude1) - rad(longitude2); //经度之差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式
        s = s * 6378137.0;//弧长乘地球半径（半径为米）
        s = Math.round(s * 10000d) / 10000d;//精确距离的数值
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; //角度转换成弧度
    }

    @Override
    public int getItemCount() {
        if (orderJsonList == null) {
            return 0;
        } else {
            return orderJsonList.size();
        }
    }
}
