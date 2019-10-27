package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.sp19.R;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DetailGiftCustomerDialog extends DialogFragment {
    private LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list = new LinkedHashMap<>();
    private String content = null;

    public void setContent(String content) {
        this.content = content;
    }

    public void setList(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list) {
        this.list = list;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_detail_gift);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout llContent = dialog.findViewById(R.id.ll_content);
        llContent.removeAllViews();
        TextView tvNumber = dialog.findViewById(R.id.tv_number);
        if(content != null){
            tvNumber.setText(content);
        }else {
            tvNumber.setVisibility(View.GONE);
        }
        int count = 0;
        for (Map.Entry<CustomerModel, List<CustomerGiftModel>> map : list.entrySet()) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inf.inflate(R.layout.item_detail_gift, null);
            View line = view.findViewById(R.id.line);
            if (count == 0) {
                line.setVisibility(View.GONE);
                count++;
            } else {
                line.setVisibility(View.VISIBLE);
            }
            TextView tvBill = view.findViewById(R.id.tv_bill);
            TextView tvPhone = view.findViewById(R.id.tv_customer_phone);
            if (content == null){

                tvPhone.setText("SĐT: "+map.getKey().getCustomerPhone());
            }else {
                tvPhone.setVisibility(View.GONE);
            }
            tvBill.setText(map.getKey().getBillNumber());
            LinearLayout llGift = view.findViewById(R.id.ll_content);
            llGift.removeAllViews();
            for (CustomerGiftModel giftModel : map.getValue()) {
                View v = inf.inflate(R.layout.item_gift_change, null);
                TextView tvGiftNumber = v.findViewById(R.id.tv_gift_number);
                tvGiftNumber.setText(giftModel.getGiftModel().getGiftName() + " - " + giftModel.getNumberGift());
                llGift.addView(v);
            }

            llContent.addView(view);
        }
        dialog.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}
