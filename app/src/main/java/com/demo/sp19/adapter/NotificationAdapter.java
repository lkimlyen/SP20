package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.NotificationModel;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationModel> list = new ArrayList<>();
    private Context context;

    public NotificationAdapter(List<NotificationModel> list, Context context) {
        this.context = context;
        this.list = list;
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        NotificationViewHolder holder = new NotificationViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        if (list != null && 0 <= position && position < list.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = list.get(position);
        holder.tvTitle.setText(notificationModel.getTitle());
        holder.tvDescription.setText(notificationModel.getDescription());
        holder.tvDate.setText(notificationModel.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvDate;

        private NotificationViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tv_date);
            tvTitle = v.findViewById(R.id.tv_title);
            tvDescription = v.findViewById(R.id.tv_description);

        }
    }

}
