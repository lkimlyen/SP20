package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.demo.architect.data.model.UserEntity;
import com.demo.sp19.R;
import com.demo.sp19.manager.UserManager;
import com.demo.sp19.screen.confirm_set.ConfirmSetActivity;
import com.demo.sp19.screen.emergency_report.EmergencyReportActivity;
import com.demo.sp19.screen.posm.POSMActivity;
import com.demo.sp19.screen.request_gift.RequestGiftActivity;
import com.demo.sp19.screen.take_off_volumn.TakeOffVolumnActivity;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.StockViewHolder> {

    private final int[] imagesIds;
    private int count = 0;
    private OnUploadListener onUploadListener;
    private OnWaitingListener onWaitingListener;
    private OnSettingListener onSettingListener;
    private Context context;
    private boolean isClick = true;

    public FunctionAdapter(int[] imagesIds, OnUploadListener onUploadListener, OnWaitingListener onWaitingListener, OnSettingListener onSettingListener, Context context) {
        this.imagesIds = imagesIds;
        this.onUploadListener = onUploadListener;
        this.onWaitingListener = onWaitingListener;
        this.onSettingListener = onSettingListener;
        this.context = context;
    }

    public void updateCount(int count) {
        this.count = count;
        notifyDataSetChanged();

    }

    public void setClickable(boolean isClick) {
        this.isClick = isClick;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        StockViewHolder holder = new StockViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        if (imagesIds != null && 0 <= position && position < imagesIds.length) {
            setDataToViews(holder, position);
        }

    }

    private void setDataToViews(StockViewHolder holder, int position) {
        Picasso.get().load(imagesIds[position]).into(holder.ivProduct);
        UserEntity userEntity = UserManager.getInstance().getUser();
        holder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    switch (position) {
                        case 0:
                            TakeOffVolumnActivity.start(context);
                            break;
                        case 1:
                            EmergencyReportActivity.start(context);
                            break;
                        case 2:
                            POSMActivity.start(context);
                            break;
                        case 3:
                            if (userEntity.getOutlet().isPromotion()) {
                                RequestGiftActivity.start(context);

                            } else {
                                startDialogNoti(context.getString(R.string.text_function_not_in_outlet), SweetAlertDialog.WARNING_TYPE);
                            }
                            break;
                        case 4:
                            if (userEntity.getOutlet().isPromotion()) {
                                ConfirmSetActivity.start(context);
                            } else {
                                startDialogNoti(context.getString(R.string.text_function_not_in_outlet), SweetAlertDialog.WARNING_TYPE);
                            }
                            break;

                        case 5:
                            if (count == 0) {
                                onWaitingListener.onWaiting(context.getString(R.string.text_no_data_upload));
                                return;
                            }
                            onUploadListener.onUploadClick();
                            break;

                        case 6:
                            onSettingListener.onSetting();

                            break;
                    }
                }
            }
        });
        holder.tvBadge.setText(String.valueOf(count));
        if (imagesIds[position] == R.drawable.ic_upload && count > 0) {
            holder.tvBadge.setVisibility(View.VISIBLE);

        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }
    }

    private void startDialogNoti(String content, int type) {
        new SweetAlertDialog(context, type)
                .setTitleText(context.getString(R.string.text_sweet_dialog_title))
                .setContentText(content)
                .setConfirmText(context.getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public int getItemCount() {
        return imagesIds.length;
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private RippleView rippleView;
        private TextView tvBadge;

        private StockViewHolder(View v) {
            super(v);
            ivProduct = v.findViewById(R.id.gridview_image);
            rippleView = (RippleView) v.findViewById(R.id.rippleView);
            tvBadge = v.findViewById(R.id.tv_badge);
        }
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
