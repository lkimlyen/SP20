package com.demo.sp19.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.sp19.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConfirmGiftDialog extends DialogFragment {
    private LinkedHashMap<GiftModel, Integer> GiftList = new LinkedHashMap<>();

    public void setGiftList(LinkedHashMap<GiftModel, Integer> giftList) {
        GiftList = giftList;
    }

    public void setListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public OnConfirmListener onConfirmListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_confirm_product);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout layout = dialog.findViewById(R.id.ll_content);
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.text_list_gift_changed));
        layout.removeAllViews();
        for (Map.Entry<GiftModel, Integer> map : GiftList.entrySet()) {
            if (map.getValue() > 0) {
                LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inf.inflate(R.layout.item_gift, null);
                ImageView ivGift = v.findViewById(R.id.iv_gift);
                ivGift.setVisibility(View.GONE);
                TextView tvName = v.findViewById(R.id.tv_name);
                tvName.setTextColor(getResources().getColor(R.color.black));
                TextView tvNumber = v.findViewById(R.id.tv_number);
                tvNumber.setTextColor(getResources().getColor(R.color.black));
                tvName.setText(map.getKey().getGiftName());
                tvNumber.setText(String.valueOf(map.getValue()));
                layout.addView(v);
            }

        }
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onConfirmListener.onConfirm();
            }
        });
        return dialog;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
