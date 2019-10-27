package com.demo.sp19.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StatisticalGiftAdapter extends RecyclerView.Adapter<StatisticalGiftAdapter.StockViewHolder> {

    private LinkedHashMap<CustomerModel, List<CustomerGiftModel>> detailGiftList = new LinkedHashMap<>();
    private List<CustomerModel> orderCodeList = new ArrayList<>();
    private Context context;

    public StatisticalGiftAdapter(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> detailGiftList, Context context) {
        this.context = context;
        this.detailGiftList = detailGiftList;
        this.orderCodeList = new ArrayList<CustomerModel>(detailGiftList.keySet());
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_statistical_gift, parent, false);
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

        CustomerModel customerModel = orderCodeList.get(position);
        holder.tvOrderId.setText(customerModel.getBillNumber());
        holder.tvSerial.setText(String.valueOf(position + 1));
        List<CustomerGiftModel> customerGiftModelList = detailGiftList.get(customerModel);
        String gift = "";
        for (CustomerGiftModel customerGiftModel : customerGiftModelList) {
            gift += customerGiftModel.getGiftModel().getGiftName() + " - " + customerGiftModel.getNumberGift() + "\n";
        }
        holder.tvGift.setText(gift);
        holder.tvCustomer.setText(customerModel.getCustomerName() + "\n" + customerModel.getCustomerPhone());
    }

    @Override
    public int getItemCount() {
        return detailGiftList.size();
    }

    public class StockViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSerial;
        private TextView tvGift;
        private TextView tvOrderId;
        private TextView tvCustomer;

        private StockViewHolder(View v) {
            super(v);
            tvSerial = v.findViewById(R.id.tv_serial);
            tvGift = v.findViewById(R.id.tv_gift);
            tvOrderId = v.findViewById(R.id.tv_order_id);
            tvCustomer = v.findViewById(R.id.tv_customer);

        }
    }

}
