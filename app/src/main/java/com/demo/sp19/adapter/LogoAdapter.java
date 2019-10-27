package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.sp19.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LogoAdapter extends RecyclerView.Adapter<LogoAdapter.ImageRealViewHolder> {

    private List<Integer> list = new ArrayList<>();
    private Context context;

    public LogoAdapter(List<Integer> list, Context context) {
        this.context = context;
        this.list = list;
    }


    @Override
    public ImageRealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logo, parent, false);
        ImageRealViewHolder holder = new ImageRealViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageRealViewHolder holder, int position) {
        if (list != null && 0 <= position && position < list.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(ImageRealViewHolder holder, int position) {
        Picasso.get().load(list.get(position)).into(holder.ivLogo);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageRealViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivLogo;
        private ImageRealViewHolder(View v) {
            super(v);
            ivLogo = (ImageView) v.findViewById(R.id.iv_logo);


        }
    }

}
