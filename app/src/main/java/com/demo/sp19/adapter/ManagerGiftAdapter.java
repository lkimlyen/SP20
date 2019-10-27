package com.demo.sp19.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.DetailCurrentGiftModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.sp19.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ManagerGiftAdapter extends RecyclerView.Adapter<ManagerGiftAdapter.StockViewHolder> {

    private LinkedHashMap<GiftModel, DetailCurrentGiftModel> detailGiftList = new LinkedHashMap<>();
    private List<GiftModel> giftList = new ArrayList<>();
    private Context context;

    public ManagerGiftAdapter(LinkedHashMap<GiftModel, DetailCurrentGiftModel> detailGiftList, Context context) {
        this.context = context;
        this.detailGiftList = detailGiftList;
        this.giftList = new ArrayList<GiftModel>(detailGiftList.keySet());
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gift, parent, false);
        StockViewHolder holder = new StockViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        if (detailGiftList != null && 0 <= position && position < detailGiftList.size()) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(StockViewHolder holder, int position) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int min = Math.min(height, width);
        float radius = min / 2.5f;
        int handRadius = (int)(radius) / 3;
        LinearLayout.LayoutParams layoutParams;
            layoutParams = new LinearLayout.LayoutParams(handRadius,handRadius);
        holder.ivGift.setLayoutParams(layoutParams);
        GiftModel giftModel = giftList.get(position);
        DetailCurrentGiftModel detailCurrentGiftModel = detailGiftList.get(giftModel);
        File file = new File(giftModel.getFilePath());
        Picasso.get().load(file).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.ivGift);
        holder.tvName.setText(giftModel.getGiftName());
        if (detailCurrentGiftModel!= null){
            holder.tvNumber.setText(String.valueOf(detailCurrentGiftModel.getNumber()));
        }else {
            holder.tvNumber.setText(String.valueOf(0));
        }
    }

    @Override
    public int getItemCount() {
        return detailGiftList.size();
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivGift;
        private TextView tvName;
        private TextView tvNumber;

        private StockViewHolder(View v) {
            super(v);
            ivGift = v.findViewById(R.id.iv_gift);
            tvName = v.findViewById(R.id.tv_name);
            tvNumber = v.findViewById(R.id.tv_number);

        }
    }

}
