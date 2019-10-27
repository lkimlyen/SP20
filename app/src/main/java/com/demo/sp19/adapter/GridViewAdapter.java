package com.demo.sp19.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.demo.sp19.R;
import com.demo.sp19.screen.emergency_report.EmergencyReportActivity;
import com.demo.sp19.screen.manager_gift.ManagerGiftActivity;
import com.demo.sp19.screen.request_gift.RequestGiftActivity;
import com.demo.sp19.screen.setting.SettingActivity;
import com.demo.sp19.screen.statistical_gift.StatisticalGiftActivity;
import com.demo.sp19.screen.take_off_volumn.TakeOffVolumnActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private final int[] imagesIds;
    private int count = 0;
    private OnUploadListener onUploadListener;
    private OnWaitingListener onWaitingListener;
    private OnSettingListener onSettingListener;

    public GridViewAdapter(Context c, int[] imagesIds, OnUploadListener onUploadListener, OnWaitingListener onWaitingListener, OnSettingListener onSettingListener) {
        mContext = c;
        this.imagesIds = imagesIds;
        this.onUploadListener = onUploadListener;
        this.onWaitingListener = onWaitingListener;
        this.onSettingListener = onSettingListener;
    }

    public void updateCount(int count) {
        this.count = count;
        notifyDataSetChanged();

    }

    @Override

    public int getCount() {
        return imagesIds.length;
    }

    @Override
    public Object getItem(int p) {
        return null;
    }

    @Override
    public long getItemId(int p) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_grid, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.gridview_image);
            Picasso.get().load(imagesIds[position]).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imageView);
            final RippleView rippleView = (RippleView) convertView.findViewById(R.id.rippleView);
            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            EmergencyReportActivity.start(mContext);
                            break;
                        case 1:
                            ManagerGiftActivity.start(mContext);
                            break;
                        case 2:
                            RequestGiftActivity.start(mContext);
                            break;
                        case 3:
                            StatisticalGiftActivity.start(mContext);
                            break;
                        case 4:
                            if (count == 0){
                                onWaitingListener.onWaiting(mContext.getString(R.string.text_no_data_upload));
                                return;
                            }
                            onUploadListener.onUploadClick();
                            break;
                        case 5:
                            onSettingListener.onSetting();
                            break;
                        case 6:

                            TakeOffVolumnActivity.start(mContext);
                            break;
                    }
                }
            });

            TextView tvBadge = convertView.findViewById(R.id.tv_badge);
            tvBadge.setText(String.valueOf(count));
            if (imagesIds[position] == R.drawable.ic_upload && count > 0) {
                tvBadge.setVisibility(View.VISIBLE);

            } else {
                tvBadge.setVisibility(View.GONE);
            }

        return convertView;
    }

    public interface OnUploadListener {
        void onUploadClick();
    }

    public interface OnWaitingListener {
        void onWaiting(String message);
    }

    public interface OnSettingListener {
        void onSetting();
    }

}

