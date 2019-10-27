package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.ReasonModel;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.List;

public class ReasonAdapter extends RecyclerView.Adapter<ReasonAdapter.ReasonViewHolder> {

    private List<ReasonModel> list = new ArrayList<>();
    private Context context;

    public void setReasonSelected(ReasonModel reasonSelected,boolean change) {
        this.reasonSelected = reasonSelected;
        this.change = change;
        notifyDataSetChanged();
    }

    private ReasonModel reasonSelected = null;
    private boolean change = true;

    public ReasonAdapter(List<ReasonModel> list, Context context) {
        this.context = context;
        this.list = list;
    }


    public ReasonModel getReasonSelected() {
        return reasonSelected;
    }

    @Override
    public ReasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reason, parent, false);
        ReasonViewHolder holder = new ReasonViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReasonViewHolder holder, int position) {
        if (list != null && 0 <= position && position < list.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(ReasonViewHolder holder, int position) {
        ReasonModel reasonModel = list.get(position);
        holder.tvReason.setText(reasonModel.getEmergencyName());
        if (change){
            holder.tvReason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reasonSelected != null && reasonSelected.equals(reasonModel)) {
                        reasonSelected = null;
                        holder.tvReason.setBackgroundResource(R.drawable.bg_textview_grey);
                    } else {
                        reasonSelected = reasonModel;
                        holder.tvReason.setBackgroundResource(R.drawable.bg_textview_green);
                        notifyDataSetChanged();
                    }
                }
            });

        }else {
            holder.tvReason.setOnClickListener(null);
        }
        if (reasonSelected != null) {
            if (reasonModel.getId() == reasonSelected.getId()) {
                holder.tvReason.setBackgroundResource(R.drawable.bg_textview_green);
            } else {
                holder.tvReason.setBackgroundResource(R.drawable.bg_textview_grey);
            }
        } else {
            holder.tvReason.setBackgroundResource(R.drawable.bg_textview_grey);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReasonViewHolder extends RecyclerView.ViewHolder {
        private TextView tvReason;

        private ReasonViewHolder(View v) {
            super(v);
            tvReason = v.findViewById(R.id.tv_reason);

        }
    }

}
