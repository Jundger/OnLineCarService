package com.jundger.carservice.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jundger.carservice.R;
import com.jundger.carservice.domain.FaultInfo;

import java.util.List;

/**
 * Created by 14246 on 2018/1/4.
 */

public class FaultInfoAdapter extends RecyclerView.Adapter<FaultInfoAdapter.ViewHolder> {

    private Context mContext;

    private List<FaultInfo> mFaultInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView faultCode;
        TextView system;
        TextView scope;
        TextView descripe;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            faultCode = itemView.findViewById(R.id.fault_code_tv);
            system = itemView.findViewById(R.id.component_system_tv);
            scope = itemView.findViewById(R.id.use_scope_tv);
            descripe = itemView.findViewById(R.id.describe_tv);
        }
    }

    public FaultInfoAdapter(List<FaultInfo> mFaultInfoList) {
        this.mFaultInfoList = mFaultInfoList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fault_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FaultInfo faultInfo = mFaultInfoList.get(position);
        holder.faultCode.setText(faultInfo.getCode());
        holder.system.setText(faultInfo.getSystem());
        holder.scope.setText(faultInfo.getScope());
        holder.descripe.setText(faultInfo.getDescripe());
    }

    @Override
    public int getItemCount() {
        return mFaultInfoList.size();
    }
}
