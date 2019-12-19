package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfirmSetAdapter extends BaseAdapter {
    private OnConfirmListener onConfirmListener;
    private List<ConfirmSetEntity> confirmList;
    private LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> brandSetList;

    public ConfirmSetAdapter(OnConfirmListener onConfirmListener, List<ConfirmSetEntity> confirmList, LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> brandSetList) {
        this.onConfirmListener = onConfirmListener;
        this.confirmList = confirmList;
        this.brandSetList = brandSetList;
    }



    @Override
    public int getCount() {
        return confirmList.size();
    }

    @Override
    public Object getItem(int position) {
        return confirmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return confirmList.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConfirmHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_confirm_set, parent, false);
            viewHolder = new ConfirmHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ConfirmHolder) convertView.getTag();
        }

        final ConfirmSetEntity item = confirmList.get(position);
        setDataToViews(viewHolder, item);

        return convertView;
    }


    private void setDataToViews(ConfirmHolder holder, ConfirmSetEntity item) {
        holder.tvDate.setText(ConvertUtils.convertLongToString(item.getCreatedAt()*1000));
        if (item.getStatus() == Constants.CONFIRMED) {
            holder.tvStatus.setText(String.format(CoreApplication.getInstance().getString(R.string.text_status),CoreApplication.getInstance().getString(R.string.text_warehouse_confirmed)));
            holder.btState.setVisibility(View.VISIBLE);
            holder.btState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onConfirmListener.onConfirm(item);
                }
            });
        }else if (item.getStatus() == Constants.RCEIVED) {
            holder.tvStatus.setText(String.format(CoreApplication.getInstance().getString(R.string.text_status),CoreApplication.getInstance().getString(R.string.text_confirm_success)));

            holder.btState.setVisibility(View.GONE);
            holder.btState.setOnClickListener(null);
        } else {
            holder.tvStatus.setText(String.format(CoreApplication.getInstance().getString(R.string.text_status),CoreApplication.getInstance().getString(R.string.text_warehouse_unconfirmed)));

            holder.btState.setVisibility(View.GONE);
            holder.btState.setOnClickListener(null);
        }
        holder.llGift.removeAllViews();
        LayoutInflater inf = (LayoutInflater) CoreApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (ConfirmSetEntity.DetailSet detailRequestGiftModel : item.getDetailSetList()) {
            View v = inf.inflate(R.layout.item_gift_product, null);
            TextView tvName = v.findViewById(R.id.tv_name);
            EditText etNumber = v.findViewById(R.id.et_number);
            etNumber.setEnabled(false);
            etNumber.setText(String.valueOf(detailRequestGiftModel.getNumber()));
            LinkedHashMap<BrandModel, BrandSetModel> linkedHashMap = brandSetList.get(detailRequestGiftModel.getBrandSetId());
            for (Map.Entry<BrandModel, BrandSetModel> map : linkedHashMap.entrySet()) {
                tvName.setText(String.format(CoreApplication.getInstance().getString(R.string.text_set_gift),
                        map.getKey().getBrandName()));
            }


            holder.llGift.addView(v);
        }

    }

    public class ConfirmHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        LinearLayout llGift;
        Button btState;
        TextView tvStatus;

        private ConfirmHolder(View v) {
            super(v);
            tvStatus = (TextView) v.findViewById(R.id.tv_status);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
            llGift = (LinearLayout) v.findViewById(R.id.ll_gift);
            btState = v.findViewById(R.id.bt_confirm);
        }

    }

    public interface OnConfirmListener {
        void onConfirm(ConfirmSetEntity item);
    }

}
